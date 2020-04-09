//
//  main.m
//  RNAnalyticsIntegration
//
//  Created by fathy on 05/08/2018.
//  Copyright © 2018 Segment.io, Inc. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <RNAnalytics/RNAnalytics.h>
#import <Adjust/Adjust.h>
#import <Segment-Adjust/SEGAdjustIntegrationFactory.h>

@interface RNAnalyticsIntegration_Adjust: NSObject<RCTBridgeModule, AdjustDelegate>
    @property RCTResponseSenderBlock callback;
@end

@implementation RNAnalyticsIntegration_Adjust

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setup) {
    [RNAnalytics addIntegration:SEGAdjustIntegrationFactory.instance];
}

RCT_EXPORT_METHOD(setupListener:(NSString *)token environment:(NSString *)environment callback:(RCTResponseSenderBlock)callback) {
    NSString *adjustConfigEnv = ![environment  isEqual: @"production"] ? ADJEnvironmentSandbox : ADJEnvironmentProduction;
    
    ADJConfig *adjustConfig = [ADJConfig configWithAppToken:token
                                                environment:adjustConfigEnv];
    
    self.callback = callback;
    [adjustConfig setDelegate:self];
    [Adjust appDidLaunch:adjustConfig];
}

RCT_EXPORT_METHOD(getAttribution:(RCTResponseSenderBlock)callback) {
    ADJAttribution *attribution = [Adjust attribution];
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    if (attribution == nil) {
        callback(@[dictionary]);
        return;
    }

    [self addValueOrEmpty:dictionary key:@"trackerToken" value:attribution.trackerToken];
    [self addValueOrEmpty:dictionary key:@"trackerName" value:attribution.trackerName];
    [self addValueOrEmpty:dictionary key:@"network" value:attribution.network];
    [self addValueOrEmpty:dictionary key:@"campaign" value:attribution.campaign];
    [self addValueOrEmpty:dictionary key:@"creative" value:attribution.creative];
    [self addValueOrEmpty:dictionary key:@"adgroup" value:attribution.adgroup];
    [self addValueOrEmpty:dictionary key:@"clickLabel" value:attribution.clickLabel];
    [self addValueOrEmpty:dictionary key:@"adid" value:attribution.adid];
    callback(@[dictionary]);
}

- (void)adjustAttributionChanged:(ADJAttribution *)attribution
{
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    if (attribution == nil) {
        self.callback(@[dictionary]);
        return;
    }
    
    [self addValueOrEmpty:dictionary key:@"trackerToken" value:attribution.trackerToken];
    [self addValueOrEmpty:dictionary key:@"trackerName" value:attribution.trackerName];
    [self addValueOrEmpty:dictionary key:@"network" value:attribution.network];
    [self addValueOrEmpty:dictionary key:@"campaign" value:attribution.campaign];
    [self addValueOrEmpty:dictionary key:@"creative" value:attribution.creative];
    [self addValueOrEmpty:dictionary key:@"adgroup" value:attribution.adgroup];
    [self addValueOrEmpty:dictionary key:@"clickLabel" value:attribution.clickLabel];
    [self addValueOrEmpty:dictionary key:@"adid" value:attribution.adid];
    
    self.callback(@[dictionary]);
}

- (void)addValueOrEmpty:(NSMutableDictionary *)dictionary
                    key:(NSString *)key
                  value:(NSObject *)value {
    if (nil != value) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", value] forKey:key];
    } else {
        [dictionary setObject:@"" forKey:key];
    }
}

@end
