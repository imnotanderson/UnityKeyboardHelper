
#import <Foundation/Foundation.h>
#import "UnityAppController.h"
#import "kbhelper.h"



@implementation KbHelper:NSObject


+(UnityAppController*)getCrl{
    UnityAppController * crl = (UnityAppController*)[UIApplication sharedApplication].delegate;
    return crl;
}

+(void)init:(int) mode{
    KbHelper* helper = [[KbHelper alloc]init];
    helper.m = mode;
    [helper kbFixInit];
}


-(void)kbFixInit{
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(willShow:) name:UIKeyboardWillChangeFrameNotification object:nil];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(willHide:) name:UIKeyboardWillHideNotification object:nil];
}

-(void)willShow:( NSNotification*) note{
    
    NSDictionary* userInfo = note.userInfo;
    NSValue* nsval = userInfo[UIKeyboardFrameEndUserInfoKey];
    CGRect bounds = [nsval CGRectValue];
    CGFloat deltaY = bounds.size.height;
    if(self.m==0){
        UIView* view = (UIView*)[ KbHelper getCrl ].unityView;
        view. transform = CGAffineTransformMakeTranslation(0, -deltaY);
    }else if (self.m==1){
        UIView* view = (UIView*)[ KbHelper getCrl ].unityView;
        CGFloat height = [view frame].size.height;
        CGFloat perc = deltaY/height;
        NSString* nsstr = [ NSString stringWithFormat:@"%f",perc ];
        UnitySendMessage("[UnityKeyboardHelper]", "Recv", [nsstr UTF8String]);
    }
}

-(void)willHide:(NSNotification*)note{
    UIView* view = (UIView*)[ KbHelper getCrl ].unityView;
    view.transform = CGAffineTransformIdentity;
    UnitySendMessage("[UnityKeyboardHelper]", "Recv", "0");}


@end

#if defined (__cplusplus)
extern "C"{
#endif
    
    extern void UnitySendMessage(const char * goName,const char * funcName,const char * param);
    
    void _Init(int mode){
        [KbHelper init:mode];
    }
#if defined (__cplusplus)
}
#endif
