package appCep.tssistemas.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layout{

public static void LS_320x480_1(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 3;BA.debugLine="paImg.Top = 0"[Layout/320x480,scale=1]
views.get("paimg").vw.setTop((int)(0d));
//BA.debugLineNum = 4;BA.debugLine="paImg.Left = 0"[Layout/320x480,scale=1]
views.get("paimg").vw.setLeft((int)(0d));
//BA.debugLineNum = 5;BA.debugLine="paImg.Width = 100%x"[Layout/320x480,scale=1]
views.get("paimg").vw.setWidth((int)((100d / 100 * width)));
//BA.debugLineNum = 7;BA.debugLine="img.Left = 5%x"[Layout/320x480,scale=1]
views.get("img").vw.setLeft((int)((5d / 100 * width)));
//BA.debugLineNum = 8;BA.debugLine="img.Width = 90%x"[Layout/320x480,scale=1]
views.get("img").vw.setWidth((int)((90d / 100 * width)));
//BA.debugLineNum = 9;BA.debugLine="img.Top = 1%y"[Layout/320x480,scale=1]
views.get("img").vw.setTop((int)((1d / 100 * height)));
//BA.debugLineNum = 10;BA.debugLine="img.Height = 10%y"[Layout/320x480,scale=1]
views.get("img").vw.setHeight((int)((10d / 100 * height)));
//BA.debugLineNum = 12;BA.debugLine="lblCep.Top = img.Top + img.Height + 4%y"[Layout/320x480,scale=1]
views.get("lblcep").vw.setTop((int)((views.get("img").vw.getTop())+(views.get("img").vw.getHeight())+(4d / 100 * height)));
//BA.debugLineNum = 14;BA.debugLine="paCep.Top  = lblCep.Top + lblCep.Height + 1%y"[Layout/320x480,scale=1]
views.get("pacep").vw.setTop((int)((views.get("lblcep").vw.getTop())+(views.get("lblcep").vw.getHeight())+(1d / 100 * height)));
//BA.debugLineNum = 15;BA.debugLine="paCep.SetLeftAndRight( 5%x, 95%x)"[Layout/320x480,scale=1]
views.get("pacep").vw.setLeft((int)((5d / 100 * width)));
views.get("pacep").vw.setWidth((int)((95d / 100 * width) - ((5d / 100 * width))));
//BA.debugLineNum = 16;BA.debugLine="lblCepText.Width = paCep.Width - 5%x"[Layout/320x480,scale=1]
views.get("lblceptext").vw.setWidth((int)((views.get("pacep").vw.getWidth())-(5d / 100 * width)));
//BA.debugLineNum = 19;BA.debugLine="lblCep.Left = paCep.Left"[Layout/320x480,scale=1]
views.get("lblcep").vw.setLeft((int)((views.get("pacep").vw.getLeft())));
//BA.debugLineNum = 21;BA.debugLine="paTeclado.Top = paCep.Top + paCep.Height + 3%y"[Layout/320x480,scale=1]
views.get("pateclado").vw.setTop((int)((views.get("pacep").vw.getTop())+(views.get("pacep").vw.getHeight())+(3d / 100 * height)));
//BA.debugLineNum = 22;BA.debugLine="paTeclado.SetLeftAndRight( 5%x, 95%x )"[Layout/320x480,scale=1]
views.get("pateclado").vw.setLeft((int)((5d / 100 * width)));
views.get("pateclado").vw.setWidth((int)((95d / 100 * width) - ((5d / 100 * width))));
//BA.debugLineNum = 24;BA.debugLine="paPesquisar.Top = paTeclado.Top + paTeclado.Height + 2%y"[Layout/320x480,scale=1]
views.get("papesquisar").vw.setTop((int)((views.get("pateclado").vw.getTop())+(views.get("pateclado").vw.getHeight())+(2d / 100 * height)));
//BA.debugLineNum = 25;BA.debugLine="paPesquisar.SetLeftAndRight( 5%x, 95%x )"[Layout/320x480,scale=1]
views.get("papesquisar").vw.setLeft((int)((5d / 100 * width)));
views.get("papesquisar").vw.setWidth((int)((95d / 100 * width) - ((5d / 100 * width))));
//BA.debugLineNum = 26;BA.debugLine="lblPesquisar.Width = 70%x ' paPesquisar.Width - 4%y"[Layout/320x480,scale=1]
views.get("lblpesquisar").vw.setWidth((int)((70d / 100 * width)));
//BA.debugLineNum = 27;BA.debugLine="lblLimpar.Left = lblPesquisar.Left + lblPesquisar.Width + 1%x"[Layout/320x480,scale=1]
views.get("lbllimpar").vw.setLeft((int)((views.get("lblpesquisar").vw.getLeft())+(views.get("lblpesquisar").vw.getWidth())+(1d / 100 * width)));

}
public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);

}
}