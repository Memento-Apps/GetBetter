//
//  SubNoteItem.swift
//  iosApp
//
//  Created by velkonost on 26.10.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct SubNoteItem: View {
    
    let item: SubNoteUI
    let onDeleteSubNote: (SubNoteUI) -> Void
    
    init(item: SubNoteUI, onDeleteSubNote: @escaping (SubNoteUI) -> Void) {
        self.item = item
        self.onDeleteSubNote = onDeleteSubNote
    }
    
    var body: some View {
        HStack {
            ScrollView(.horizontal, showsIndicators: false) {
                Text(item.text)
                    .style(.titleSmall)
                    .lineLimit(1)
                    .foregroundColor(.textSecondaryTitle)
                    .padding(.leading, 12)
            }
            
            Spacer()
            
            ZStack {
                Image(uiImage: SharedR.images().ic_close.toUIImage()!)
                    .resizable()
                    .renderingMode(.template)
                    .foregroundColor(.iconInactive)
                    .frame(width: 24, height: 24, alignment: .center)
                    .onTapGesture {
                        onDeleteSubNote(item)
                    }
            }
            .frame(width: 36, height: 36, alignment: .center)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(Color.backgroundItem)
            )
            .padding(.trailing, 12)
        }
        .frame(height: 56)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(Color.textFieldBackground)
        )
    }
}
