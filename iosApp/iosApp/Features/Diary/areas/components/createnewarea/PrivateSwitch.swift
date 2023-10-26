//
//  PrivateSwitch.swift
//  iosApp
//
//  Created by velkonost on 04.10.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct PrivateSwitch: View {
    
    @Binding var isPrivate: Bool
    let onCheckedChange: () -> Void
    
    let isEnabled: Bool
    
    init(onCheckedChange: @escaping () -> Void, isEnabled: Bool = true, isPrivate: Binding<Bool>) {
        self.onCheckedChange = onCheckedChange
        self.isEnabled = isEnabled
        self._isPrivate = isPrivate
    }
    
    var body: some View {
        Toggle(
            isOn: Binding(
                get: { isPrivate },
                set: { newValue in
                    onCheckedChange()
                }
            )
        )
        {
            Text(isPrivate ? SharedR.strings().private_state.desc().localized() : SharedR.strings().public_state.desc().localized())
                .style(.titleMedium)
                .foregroundColor(.textSecondaryTitle)
        }
        .disabled(!isEnabled)
        .tint(.iconActive)
        .padding(.top, 12)
    }
}
