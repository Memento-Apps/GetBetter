//
//  AbilityDetailsHeader.swift
//  iosApp
//
//  Created by velkonost on 14.12.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct AbilityDetailsHeader: View {
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    private let title: String
    private let imageAlpha: CGFloat
    
    init(title: String, imageAlpha: CGFloat) {
        self.title = title
        self.imageAlpha = imageAlpha
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
                .opacity(imageAlpha)
                .animation(.easeInOut, value: imageAlpha)
            
            Text(title)
                .style(.headlineSmall)
                .foregroundColor(.textTitle)
                .padding(.leading, 12)
            Spacer()
        }
    }
}
