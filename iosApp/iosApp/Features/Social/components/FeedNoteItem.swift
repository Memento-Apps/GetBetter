//
//  FeedNoteItem.swift
//  iosApp
//
//  Created by velkonost on 05.11.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK
import SwiftUIFlow
import CachedAsyncImage

struct FeedNoteItem: View {
    
    let item: Note
    let onClick: (Note) -> Void
    let onLikeClick: (Note) -> Void
    
    init(item: Note, onClick: @escaping (Note) -> Void, onLikeClick: @escaping (Note) -> Void) {
        self.item = item
        self.onClick = onClick
        self.onLikeClick = onLikeClick
    }
    
    var body: some View {
        PrimaryBox {
            VStack(spacing: 0) {
                
                NoteItemHeader(
                    areaName: item.area.name,
                    taskName: item.task?.name,
                    areaIcon: Emoji.companion.getIconById(id: Int32(truncating: item.area.emojiId!)).toUIImage()!,
                    likesData: item.likesData,
                    onLikeClick: {
                        onLikeClick(item)
                    }
                )
                
                VStack(alignment: .leading) {
                    
                    NoteItemData(
                        noteType: item.noteType,
                        completionDate: item.completionDateStr?.localized(),
                        expectedCompletionDate: item.expectedCompletionDateStr?.localized(),
                        expectedCompletionDateExpired: item.expectedCompletionDateExpired,
                        subNotes: item.subNotes,
                        mediaAmount: item.mediaUrls.count,
                        isPrivate: item.isPrivate
                    )
                    
                    Text(item.text)
                        .style(.bodyMedium)
                        .foregroundColor(.textTitle)
                        .lineLimit(10)
                        .multilineTextAlignment(.leading)
                        .frame(minWidth: 0, maxWidth: .infinity)
                        .padding(.top, 12)
                    
                    VFlow(alignment: .leading, spacing: 3) {
                        ForEach(item.tags, id: \.self) { tag in
                            TagItem(tag: TagUI(id: "0", text: tag))
                        }
                    }
                    .padding(.top, 12)
                }
                .padding(8)
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color.textFieldBackground)
                )
                .padding(.top, 12)
                
                HStack(spacing: 0) {
                    if item.author?.avatarUrl != nil {
                        CachedAsyncImage(url: URL(string: item.author!.avatarUrl!)) { image in
                            image
                                .resizable()
                                .scaledToFill()
                                .frame(width: 24, height: 24)
                                .clipped()
                                .cornerRadius(8)
                        } placeholder: {
                            Image(uiImage: SharedR.images().logo.toUIImage()!)
                                .resizable()
                                .frame(width: 24, height: 24)
                                .clipped()
                                .cornerRadius(8)
                        }
                    } else {
                        Image(uiImage: SharedR.images().logo.toUIImage()!)
                            .resizable()
                            .frame(width: 24, height: 24)
                            .clipped()
                            .cornerRadius(8)
                    }
                    
                    if item.author?.displayName != nil {
                        Text(item.author!.displayName!)
                            .style(.bodySmall)
                            .foregroundColor(.textPrimary)
                            .padding(.leading, 6)
                    }
                    
                    Spacer()
                    
                    Text(item.createdDateStr.localized())
                        .style(.bodySmall)
                        .foregroundColor(.textPrimary)
                }
                .padding(.top, 6)
                .padding(.horizontal, 4)
                
                
            }
        }
        .onTapGesture {
            onClick(item)
            let impactMed = UIImpactFeedbackGenerator(style: .medium)
            impactMed.impactOccurred()
        }
    }
}
