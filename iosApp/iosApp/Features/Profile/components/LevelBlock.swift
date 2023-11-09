//
//  LevelBlock.swift
//  iosApp
//
//  Created by velkonost on 09.11.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct LevelBlock: View {
    
    private let experienceData: ExperienceData
    
    init(experienceData: ExperienceData) {
        self.experienceData = experienceData
    }
    
    var body: some View {
        @State var remainExperience = Double(experienceData.remainExperience)
        @State var currentLevelExperienceLimit = Double(experienceData.currentLevelExperienceLimit)
        
        PrimaryBox {
            VStack(spacing: 0) {
                HStack {
                    Spacer()
                    Text("Level \(experienceData.currentLevel)")
                        .style(.labelMedium)
                        .foregroundColor(.textPrimary)
                }
                
                ProgressView(value: 30, total: currentLevelExperienceLimit)
                    .padding(.top, 6)
            }
        }
        .padding(.top, 20)
    }
}