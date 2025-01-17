//
//  PlaceholderView.swift
//  iosApp
//
//  Created by velkonost on 19.12.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Lottie
import SharedSDK

struct PlaceholderView: View {
    
    private let text: String
    
    init(text: String) {
        self.text = text
        selectedAnimation = animations.randomElement()!
    }
    
    var selectedAnimation: FileResource
    let animations = [
        SharedR.files().anim_placeholder_1,
        SharedR.files().anim_placeholder_2,
        SharedR.files().anim_placeholder_3,
        SharedR.files().anim_placeholder_5,
        SharedR.files().anim_placeholder_6
    ]
    
    var body: some View {
        VStack(spacing: 0) {
            Spacer()
            PlaceholderLottieView(
                animation: selectedAnimation
            )
            .frame(width: 200, height: 200)
            .scaleEffect(0.7)
            .padding(.bottom, 12)
            
            Text(text)
                .style(.bodyMedium)
                .foregroundColor(.textTitle)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
            Spacer()
        }
    }
}


struct PlaceholderLottieView: UIViewRepresentable {
    
    let animation: FileResource
    
    func updateUIView(_ uiView: UIViewType, context: Context) {
        
    }
    
    func makeUIView(context: Context) -> Lottie.LottieAnimationView {
        
        let animationView = LottieAnimationView(
            filePath: animation.path
        )
        
        //        animationView.contentMode = .scaleAspectFit
        animationView.contentMode = .scaleToFill
        animationView.loopMode = .loop
        animationView.play()
        
        return animationView
    }
}
