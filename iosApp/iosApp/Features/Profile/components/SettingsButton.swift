//
//  SettingsButton.swift
//  iosApp
//
//  Created by velkonost on 20.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct SettingsButton: View {
    
    let onClick: () -> Void
    
    init(onClick: @escaping () -> Void) {
        self.onClick = onClick
    }
    
    var body: some View {
        ZStack {
            Image(uiImage: SharedR.images().ic_settings.toUIImage()!)
                .resizable()
                .renderingMode(.template)
                .frame(width: 24, height: 24, alignment: .center)
                .foregroundColor(.iconActive).opacity(0.5)
        }
        .frame(width: 48, height: 48)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color.backgroundIcon)
                .shadow(radius: 8)
        )
        .onTapGesture {
            let impactMed = UIImpactFeedbackGenerator(style: .medium)
            impactMed.impactOccurred()
            onClick()
        }
    }
}
