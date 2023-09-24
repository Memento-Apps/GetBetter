//
//  ProfileScreen.swift
//  iosApp
//
//  Created by velkonost on 20.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SharedSDK
import SwiftUI

struct ProfileScreen: View {
    
    let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String

    
    var body: some View {
        ScrollView(showsIndicators: false) {
            VStack {
                ProfileHeader(userName: "velkonost") {
                    
                } onSettingsClick: {
                    
                }
                
                SubscriptionBox(subscriptionPlan: SharedR.strings().profile_sub_basic.desc().localized()) {
                    
                }

                AppSettings()
                HelpAndSupport()
                
                HStack {
                    Spacer()
                    Text(appVersion?.uppercased() ?? "")
                        .style(.labelMedium)
                        .foregroundColor(.textUnimportantColor)
                    Spacer()
                }.padding(.top, 40)
                
            }
            .padding(.init(top: 16, leading: 16, bottom: 200, trailing: 16))   
        }
    }
    
}
