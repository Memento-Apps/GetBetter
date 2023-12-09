//
//  CalendarsScreen.swift
//  iosApp
//
//  Created by velkonost on 21.09.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import SharedSDK
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct ActionUIItemWrapper : Identifiable, Equatable, Hashable {
    var id: String = UUID().uuidString
    var item: ActionUIItem<AnyObject, AnyObject>
}

struct CalendarsScreen: View {
    
    @StateViewModel var viewModel: CalendarsViewModel
    @State private var scrollTarget: Int64?
    
    @State private var selectedAreaId: Int32? = nil
    @State private var selectedUserId: String? = nil
    
    @State private var showingAreaDetailSheet = false
    @State private var showingProfileDetailSheet = false
    
    var body: some View {
        @State var state = viewModel.viewStateValue as! CalendarsViewState
        
        VStack(spacing: 0) {
            if state.datesState.selectedDate != nil {
                VStack(spacing: 0) {
                    HStack {
                        Text(state.datesState.selectedDate!.monthDay.localized().capitalized)
                            .style(.headlineSmall)
                            .foregroundColor(.textSecondaryTitle)
                        Spacer()
                    }
                    
                    HStack {
                        Text(state.datesState.selectedDate!.year.localized())
                            .style(.bodyLarge)
                            .foregroundColor(.textPrimary)
                            .padding(.top, 6)
                        Spacer()
                    }
                }
                .padding(.horizontal, 16)
                .animation(.easeInOut, value: state.datesState.selectedDate)
            }
            
            ScrollViewReader { view in
                ScrollView(.horizontal, showsIndicators: false) {
                    LazyHStack {
                        Spacer().frame(width: 16)
                        ForEach(state.datesState.items, id: \.self.id) { item in
                            CalendarDateItem(
                                item: item,
                                isSelected: item.id == state.datesState.selectedDate?.id,
                                onClick: { value in
                                    viewModel.dispatch(action: CalendarsActionDateClick(id: value))
                                }
                            )
                            .id(item.id)
                            .onAppear {
                                checkPaginationThreshold(currentItemId: item.id)
                            }
                            
                        }
                    }
                    .onChange(of: scrollTarget) { target in
                        if let target = target {
                            scrollTarget = nil
                            
                            withAnimation {
                                view.scrollTo(target, anchor: .center)
                            }
                        }
                    }
                }
            }
            .onChange(of: state.datesState.selectedDate) { value in
                if value != nil {
                    scrollTarget = value!.id
                }
            }
            .frame(height: 70)
            .padding(.top, 12)
            
            if state.datesState.selectedDate != nil {
                if state.datesState.selectedDate!.isLoading {
                    ZStack {
                        Spacer()
                        Loader()
                        Spacer()
                    }.frame(maxHeight: .infinity)
                } else {
                    ZStack {
                        ScrollView(showsIndicators: false) {
                            LazyVStack {
                                ForEach(
                                    state.datesState.selectedDate!.items.map { ActionUIItemWrapper(item: $0) },
                                    id: \.self.id
                                ) { wrapper in
                                    ActionItem(
                                        item: wrapper.item,
                                        onAreaClick: { value in
                                            selectedAreaId = value
                                            showingAreaDetailSheet = true
                                        },
                                        onNoteClick: { value in
                                            viewModel.dispatch(action: CalendarsActionNoteClick(value: value))
                                        },
                                        onUserClick: {
                                            selectedUserId = (wrapper.item.data as! UserInfoShort).id
                                            showingProfileDetailSheet = true
                                        },
                                        onTaskClick: { value in
                                            viewModel.dispatch(action: CalendarsActionTaskClick(value: value))
                                        }
                                    )
                                }
                                Spacer().frame(height: 160)
                            }
                            
                            .padding(.horizontal, 20)
                        }.fadingEdge()
                    }
                }
            }
            
            
        }
        .frame(maxHeight: .infinity)
        .sheet(isPresented: $showingAreaDetailSheet) {
            AreaDetailScreen(
                areaId: $selectedAreaId,
                onClose: {
                    showingAreaDetailSheet = false
                },
                onAreaChanged: { areaId in
                    //                    viewModel.dispatch(action: NoteDetailActionAreaChanged())
                }
            )
        }
        .sheet(isPresented: $showingProfileDetailSheet) {
            ProfileDetailScreen(userId: $selectedUserId)
        }
        
    }
}

extension CalendarsScreen {
    func checkPaginationThreshold(currentItemId: Int64) {
        let state = viewModel.viewStateValue as! CalendarsViewState
        let data = state.datesState.items
        let thresholdIndexRight = data.index(data.endIndex, offsetBy: -5)
        
        
        if data.firstIndex(where: { $0.id == currentItemId })! >= thresholdIndexRight && !state.datesState.isNextLoading {
            viewModel.dispatch(action: CalendarsActionLoadMoreNextDates())
        }
    }
}
