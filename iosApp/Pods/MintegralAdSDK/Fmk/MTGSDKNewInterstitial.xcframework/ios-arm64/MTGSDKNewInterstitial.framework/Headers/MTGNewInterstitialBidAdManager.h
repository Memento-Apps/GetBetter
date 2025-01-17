//
//  MTGNewInterstitialBidAdManager.h
//  MTGSDKNewInterstitial
//
//  Created by Harry on 2022/1/7.
//  Copyright © 2022 Mintegral. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "MTGSDKNewInterstitial.h"

NS_ASSUME_NONNULL_BEGIN

@interface MTGNewInterstitialBidAdManager : NSObject

@property(nonatomic, readonly, weak) id <MTGNewInterstitialBidAdDelegate> _Nullable
delegate;

@property(nonatomic, readonly, copy) NSString *_Nonnull
currentUnitId;

@property(nonatomic, readonly, copy) NSString *_Nullable
placementId;

/**
 * Play the video is mute in the beginning ,defult is NO
 *
 */
@property(nonatomic, assign) BOOL playVideoMute;

- (nonnull instancetype)initWithPlacementId:(nonnull NSString

*)
placementId
        unitId
:(
nonnull NSString
*)
unitId
        delegate
:(
nullable id
<MTGNewInterstitialBidAdDelegate>)
delegate;


/**
 Begins loading header bidding ad content.

 @param bidToken token from bid request.
*/
- (void)loadAdWithBidToken:(nonnull NSString

*)
bidToken;

/**
 Whether or not if there was a available bidding ad to show.
 
 @return YES means there was a available bidding ad, otherwise NO.
*/
- (BOOL)isAdReady;

/**
 * Presents the NewInterstitial ad modally from the specified view controller.
 *
 * @param viewController The view controller that should be used to present the  ad.
 */
- (void)showFromViewController:(UIViewController *_Nonnull)viewController;


/**
  * Set NewInterstitial  reward if you need，call before loadAd.
  * @param rewardMode  {@link MTGNIRewardMode} for list of supported types
  * @param playRate Set the timing of the reward alertView,range of 0~1(eg:set 0.6,indicates 60%).
  NOTE:In MTGNIRewardPlayMode, playRate value indicates that a reward alertView will appear when the playback reaches the set playRate.
       In MTGNIRewardCloseMode, playRate value indicates that when the close button is clicked, if the video playback time is less than the set playRate, reward alertView will appear.
 */
- (void)setRewardMode:(MTGNIRewardMode)rewardMode playRate:(CGFloat)playRate;

/**
 * Set NewInterstitial reward if you need，call before loadAd.
 * @param rewardMode  {@link MTGNIRewardMode} for list of supported types
 * @param playTime Set the timing of the reward alertView,range of 0~100s.
 NOTE:In MTGNIRewardPlayMode, playTime value indicates that a reward alertView will appear when the playback reaches the set playTime.
      In MTGNIRewardCloseMode, playTime value indicates that when the close button is clicked, if the video playback time is less than the set playTime, reward alertView will appear.
*/
- (void)setRewardMode:(MTGNIRewardMode)rewardMode playTime:(NSInteger)playTime;

/**
*  Call this method when you want custom the reward alert  display text.
*
* @param title  alert title
* @param content    alertcontent
* @param confirmText    confirm button text
* @param cancelText     cancel button text
 
 NOTE:Must be called before loadAd
*/
- (void)setAlertWithTitle:(NSString *_Nullable)title
                  content:(NSString *_Nullable)content
              confirmText:(NSString *_Nullable)confirmText
               cancelText:(NSString *_Nullable)cancelText;

/**
* get the id of this request ad, call this  after newInterstitialBidAdLoadSuccess.
*/
- (NSString *_Nullable)getRequestIdWithUnitId:(nonnull NSString

*)
unitId;

/// get the creativeId of this requested ad, call this after newInterstitialBidAdLoadSuccess.
- (NSString *_Nullable)getCreativeIdWithUnitId:(nonnull NSString

*)
unitId;


/// Pass extra info into sdk.
/// @param extraInfo info you want to pass
/// @param key corresponding key for extraInfo
- (void)setExtraInfo:(id _Nullable)extraInfo forKey:(NSString *_Nonnull)key;

@end

NS_ASSUME_NONNULL_END
