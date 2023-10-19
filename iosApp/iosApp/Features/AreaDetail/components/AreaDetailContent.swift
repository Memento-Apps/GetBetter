//
//  AreaDetailContent.swift
//  iosApp
//
//  Created by velkonost on 08.10.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK

struct AreaDetailContent: View {
    
    let areaData: AreaDetailUI
    var isEditing: Bool
    
    @Binding var isEmojiPickerVisible: Bool
    let onEmojiClick: (Emoji) -> Void
    let onNameChanged: (String) -> Void
    let onDescriptionChanged: (String) -> Void
    
    init(areaData: AreaDetailUI, isEditing: Bool, isEmojiPickerVisible: Binding<Bool>, onEmojiClick: @escaping (Emoji) -> Void, onNameChanged: @escaping (String) -> Void, onDescriptionChanged: @escaping (String) -> Void) {
        self.areaData = areaData
        self.isEditing = isEditing
        self._isEmojiPickerVisible = isEmojiPickerVisible
        self.onEmojiClick = onEmojiClick
        self.onNameChanged = onNameChanged
        self.onDescriptionChanged = onDescriptionChanged
    }
    
    var body: some View {
        ScrollView(showsIndicators: false) {
            VStack {
                HStack {
                    Spacer()
                    SelectedEmojiImage(
                        selectedEmoji: areaData.emoji.icon.toUIImage()!,
                        imageSize: 96,
                        onClick: {
                            if isEditing {
                                withAnimation {
                                    isEmojiPickerVisible.toggle()
                                }
                            }
                        }
                    )
                    Spacer()
                }
                
                EmojiPicker(
                    isVisible: $isEmojiPickerVisible,
                    items: Emoji.entries,
                    onEmojiClick: onEmojiClick
                )
                
                SingleLineTextField(
                    value: areaData.name,
                    placeholderText: SharedR.strings().diary_areas_create_new_name_hint.desc().localized(),
                    isEnabled: isEditing,
                    textAlign: .center,
                    textStyle: .titleLarge,
                    paddings: EdgeInsets.init(),
                    onValueChanged: onNameChanged
                ).padding(.top, 24)
                
                MultilineTextField(
                    value: areaData.description_,
                    placeholderText: SharedR.strings().diary_areas_create_new_description_hint.desc().localized(),
                    minLines: 1,
                    isEnabled: isEditing, 
                    textAlign: .center,
                    paddings: .init(top: 4, leading: .zero, bottom: .zero, trailing: .zero),
                    onValueChanged: onDescriptionChanged
                )
                
                AreaDataContent
                    .opacity(!isEditing ? 1 : 0)
                    .animation(.easeInOut, value: isEditing)
                
            }.padding(20)
        }
        
    }
}

extension AreaDetailContent {
    private var AreaDataContent: some View {
        HStack {
            Text("14000\nmembers")
                .style(.titleSmall)
                .foregroundColor(.textSecondaryTitle)
                .multilineTextAlignment(.center)
                .frame(width: 80)
                .padding(.top, 4)
                .padding(.bottom, 4)
                .padding(.leading)
                .padding(.trailing)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color.backgroundIcon)
                )
            Spacer()
            Text("20200\nnotes")
                .style(.titleSmall)
                .foregroundColor(.textSecondaryTitle)
                .multilineTextAlignment(.center)
                .frame(width: 80)
                .padding(.top, 4)
                .padding(.bottom, 4)
                .padding(.leading)
                .padding(.trailing)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color.backgroundIcon)
                )
            Spacer()
            Text("20200\ntasks")
                .style(.titleSmall)
                .foregroundColor(.textSecondaryTitle)
                .multilineTextAlignment(.center)
                .frame(width: 80)
                .padding(.top, 4)
                .padding(.bottom, 4)
                .padding(.leading)
                .padding(.trailing)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color.backgroundIcon)
                )
        }
        .padding(.top, 4)
    }
}