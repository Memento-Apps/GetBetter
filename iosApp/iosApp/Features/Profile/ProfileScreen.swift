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
import PhotosUI
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct ProfileScreen: View {
    
    @Environment(\.openURL) var openURL

    @StateViewModel var viewModel: ProfileViewModel
    @State private var eventsObserver: Task<(), Error>? = nil
    
    let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
    
    @State var showImagePicker: Bool = false
    
    @State private var selectedItem: PhotosPickerItem? = nil
    @State private var selectedImageData: Data? = nil
    
    @State private var webViewVisible: Bool = false
    @State private var webViewLink: String = "http://google.com"
    
    var body: some View {
        @State var state = viewModel.viewStateValue as! ProfileViewState
        
        ScrollView(showsIndicators: false) {
            VStack {
                ProfileHeader(
                    userName: state.userName,
                    avatarUrl: state.avatarUrl,
                    isLoading: state.isLoading,
                    isAnonymous: state.isUserAnonymous,
                    onAvatarClick: {
                        self.showImagePicker.toggle()
                    },
                    onSettingsClick: {
                        viewModel.dispatch(action: SettingsClick())
                    },
                    onSignUpClick: {
                        viewModel.dispatch(action: SignUpClick())
                    }
                )
        
                
                Spacer().frame(height: 20)
                
                if state.experienceData != nil {
                    LevelBlock(experienceData: state.experienceData!)
                }
                
                if state.subscriptionData.available {
                    SubscriptionBox(
                        subscriptionPlan: state.subscriptionData.subscription?.isActive == true ? SharedR.strings().profile_sub_premium.desc().localized() : SharedR.strings().profile_sub_basic.desc().localized(),
                        buttonText: state.subscriptionData.subscription?.isActive == true ? SharedR.strings().profile_sub_more.desc().localized() : SharedR.strings().profile_sub_upgrade.desc().localized(),
                        onUpgradeClick: {
                            viewModel.dispatch(action: SubscriptionClick())
                        }
                    )
                }
                
                if !state.isUserAnonymous && state.subscriptionData.available && state.subscriptionData.subscription?.trialUsed == false {
                    TrialButton(isLoading: state.isLoading) {
                        viewModel.dispatch(action: StartTrialClick())
                    }
                }
                
                AppSettings(
                    selectedTheme: state.selectedTheme,
                    onThemeChanged: { value in
                        viewModel.dispatch(action: ThemeChange(value: value))
                    }
                )
                
                HelpAndSupport(
                    onContactUsClick: {
                        viewModel.dispatch(action: ContactUsClick())
                    },
                    onTelegramClick: {
                        let link = SharedR.strings().tg_link.desc().localized()
                        openURL(URL(string: link)!)
                    }
                )
                
                OtherBlock(
                    onPrivacyClick: {
                        webViewLink = state.privacyLink
                        webViewVisible = true
                    },
                    onTermsClick: {
                        webViewLink = state.termsLink
                        webViewVisible = true
                    }
                )
                
                AppButton(
                    labelText: SharedR.strings().profile_logout.desc().localized(),
                    isLoading: state.isLogoutLoading
                ) {
                    viewModel.dispatch(action: LogoutClick())
                }
                .padding(.top, 48)
                
                HStack {
                    Spacer()
                    Text(appVersion?.uppercased() ?? "")
                        .style(.labelMedium)
                        .foregroundColor(.textUnimportantColor)
                    Spacer()
                }.padding(.top, 40)
            }
            .padding(.init(top: 16, leading: 16, bottom: 200, trailing: 16))
            .animation(.easeInOut, value: state.experienceData)
            .animation(.easeInOut, value: state.subscriptionData)
        }
        .sheet(isPresented: $showImagePicker) {
            ImagePicker(sourceType: .photoLibrary) { image in
                Task {
                    await uploadAvatar(selectedImage: image)
                }
            }.edgesIgnoringSafeArea(.all)
        }
        .sheet(isPresented: $webViewVisible) {
            AppWebView(link: $webViewLink, isVisible: $webViewVisible)
                .ignoresSafeArea(.all)
        }
        .onAppear {
            viewModel.onAppear()
            observeEvents()
        }
        .onDisappear {
            eventsObserver?.cancel()
            eventsObserver = nil
        }
    }
    
}

extension ProfileScreen {
    func uploadAvatar(selectedImage: UIImage?) async {
        _ = Task.detached {
            if selectedImage != nil {
                let data = KotlinByteArray.from(data: selectedImage!.jpegData(compressionQuality: 0.3)!)
                await viewModel.dispatch(action: AvatarSelected(avatarContent: data))
            }
        }
        
    }
    
    func observeEvents() {
        if eventsObserver == nil {
            eventsObserver = Task {
                for try await event in asyncSequence(for: viewModel.events) {
                    switch(event) {
                    case _ as ProfileEventThemeChanged: do {
                        (UIApplication.shared.connectedScenes.first as? UIWindowScene)?.windows.first!.overrideUserInterfaceStyle = switch((event as! ProfileEventThemeChanged).value) {
                        case UIThemeMode.lighttheme: .light
                        case UIThemeMode.darktheme: .dark
                        default : .unspecified
                        }
                    }
                   
                    default:
                        break
                    }
                }
            }
        }
    }
}


