//
//  TaskDetailHeader.swift
//  iosApp
//
//  Created by velkonost on 04.12.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct TaskDetailHeader : View {
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    private let isFavorite: Bool
    private let isFavoriteLoading: Bool
    private let onFavoriteClick: () -> Void
    
    init(isFavorite: Bool, isFavoriteLoading: Bool, onFavoriteClick: @escaping () -> Void) {
        self.isFavorite = isFavorite
        self.isFavoriteLoading = isFavoriteLoading
        self.onFavoriteClick = onFavoriteClick
    }
    
    var body: some View {
        HStack {
            Image(uiImage: SharedR.images().ic_arrow_back.toUIImage()!)
                .resizable()
                .renderingMode(.template)
                .foregroundColor(.iconActive)
                .frame(width: 32, height: 32)
                .scaledToFill()
                .padding(4)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color.backgroundIcon)
                )
                .onTapGesture {
                    let impactMed = UIImpactFeedbackGenerator(style: .medium)
                    impactMed.impactOccurred()
                    presentationMode.wrappedValue.dismiss()
                }
            
            Text(SharedR.strings().task_detail_title.desc().localized())
                .style(.headlineSmall)
                .foregroundColor(.textTitle)
                .padding(.leading, 12)
            Spacer()
            
            
            ZStack(alignment: .center) {
                if (!isFavoriteLoading) {
                    VStack {
                        Image(
                            uiImage: isFavorite ? SharedR.images().ic_star.toUIImage()! : SharedR.images().ic_empty_star.toUIImage()!
                        )
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(.buttonGradientStart)
                        .scaledToFill()
                        .frame(width: 28, height: 28)
                    }
                    .onTapGesture {
                        onFavoriteClick()
                    }
                } else {
                    HStack {
                        Spacer()
                        Loader()
                            .scaleEffect(0.5)
                        Spacer()
                    }
                }
            }
            .frame(width: 32, height: 32)
            .animation(.easeInOut, value: isFavoriteLoading)
            
        }
    }
}
