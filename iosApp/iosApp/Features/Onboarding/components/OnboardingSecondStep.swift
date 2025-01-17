//
//  OnboardingSecondStep.swift
//  iosApp
//
//  Created by velkonost on 18.12.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct OnboardingSecondStep: View {
    
    @Binding var textVisible: Bool
    @Binding var buttonVisible: Bool
    
    init(textVisible: Binding<Bool>, buttonVisible: Binding<Bool>) {
        self._textVisible = textVisible
        self._buttonVisible = buttonVisible
    }
    
    @State var imageIndex: Int = 0
    private let durationPerImage: Double = 3.5
    
    @State var flipped: Bool = false
    
    private let images = [
        SharedR.images().ic_onboarding_2_1,
        SharedR.images().ic_onboarding_2_2,
        SharedR.images().ic_onboarding_2_3,
        SharedR.images().ic_onboarding_2_4,
        SharedR.images().ic_onboarding_2_5
    ]
    
    var body: some View {
        @State var rotation: Angle = Angle(degrees: Double(imageIndex) * Double(180))
        
        ZStack {
            if flipped {
                Image(uiImage: images[imageIndex].toUIImage()!)
                    .resizable()
                    .shadow(radius: 8)
                    .scaledToFit()
                    .frame(maxWidth: .infinity)
                    .padding(.horizontal, 32)
                    .rotation3DEffect(
                        Angle(degrees: Double(180)),
                        axis: (x: 0.0, y: 1.0, z: 0.0)
                    )
            } else {
                Image(uiImage: images[imageIndex].toUIImage()!)
                    .resizable()
                    .shadow(radius: 8)
                    .scaledToFit()
                    .frame(maxWidth: .infinity)
                    .padding(.horizontal, 32)
            }
        }
        .rotation3DEffect(
            rotation,
            axis: (x: 0.0, y: 1.0, z: 0.0)
        )
        .animation(.easeInOut(duration: 1), value: rotation)
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + durationPerImage) {
                imageIndex += 1
                flipped.toggle()
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 2 * durationPerImage) {
                imageIndex += 1
                flipped.toggle()
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 3 * durationPerImage) {
                imageIndex += 1
                flipped.toggle()
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 4 * durationPerImage) {
                imageIndex += 1
                flipped.toggle()
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 2 * durationPerImage + 1) {
                withAnimation(.easeInOut(duration: 0.5)) {
                    textVisible = true
                }
                withAnimation(.easeInOut(duration: 0.5).delay(0.5)) {
                    buttonVisible = true
                }
            }
            
        }
    }
}
