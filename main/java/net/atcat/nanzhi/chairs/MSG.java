package net.atcat.nanzhi.chairs;

import net.minecraft.util.text.TranslationTextComponent;

public class MSG {

    public static class msg_cls {
       protected final String str ;
       protected msg_cls ( String s ) {
           this.str = s ;
       } ;
       public TranslationTextComponent get ( ) {
           return new TranslationTextComponent( "msg." +NZChairs.ID +"." +this.str ) ;
       }
    } ;

    public static msg_cls wrong_pose = new msg_cls( "wrong_pose" ) ;
    public static msg_cls can_not_sit = new msg_cls( "can_not_sit" ) ;
    public static msg_cls too_crowded = new msg_cls( "too_crowded" ) ;
    public static msg_cls ass_cancer = new msg_cls( "ass_cancer" ) ;
    public static msg_cls comfortable_butt = new msg_cls( "comfortable_butt" ) ;

}
