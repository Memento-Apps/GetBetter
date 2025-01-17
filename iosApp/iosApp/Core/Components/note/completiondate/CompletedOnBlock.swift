//
//  CompletedOnBlock.swift
//  iosApp
//
//  Created by velkonost on 04.11.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct CompletedOnBlock: View {
    
    let label: String?
    let onClick: () -> Void
    
    init(label: String?, onClick: @escaping () -> Void) {
        self.label = label
        self.onClick = onClick
    }
    
    var body: some View {
        HStack {
            if label != nil {
                Text(SharedR.strings().note_detail_completed_goal_title.desc().localized())
                    .style(.titleMedium)
                    .multilineTextAlignment(.leading)
                    .foregroundColor(.textPrimary)
                    .padding(.leading, 16)
                
                Spacer()
                
                Text(label!)
                    .style(.bodyMedium, withSize: 16)
                    .multilineTextAlignment(.leading)
                    .foregroundColor(.textLight)
                    .padding(.horizontal, 19)
                    .padding(.vertical, 6)
                    .background(
                        RoundedRectangle(cornerRadius: 8)
                            .fill(Color.buttonGradientStart)
                    )
                    .padding(.trailing, 16)
                    .onTapGesture {
                        onClick()
                    }
            }
        }
        .frame(height: 60)
        .animation(.easeInOut, value: label)
    }
}
