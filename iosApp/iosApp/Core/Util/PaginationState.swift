//
//  PaginationState.swift
//  iosApp
//
//  Created by velkonost on 06.10.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

/// Represents the different states of a pagination.
enum PaginationState: Equatable {
    /// The error state; use this state if an error occurs while loading a page.
    case error(_ error: NSError)
    /// The idle state; use this state if no page loading is in progress.
    case idle
    /// The loading state; use this state if a page is loaded.
    case loading
}
