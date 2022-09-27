package appCep.tssistemas;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "appCep.tssistemas", "appCep.tssistemas.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "appCep.tssistemas", "appCep.tssistemas.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "appCep.tssistemas.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblceptext = null;
public appCep.tssistemas.starter _starter = null;
public appCep.tssistemas.httputils2service _httputils2service = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static void  _activity_create(boolean _firsttime) throws Exception{
ResumableSub_Activity_Create rsub = new ResumableSub_Activity_Create(null,_firsttime);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Create extends BA.ResumableSub {
public ResumableSub_Activity_Create(appCep.tssistemas.main parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
appCep.tssistemas.main parent;
boolean _firsttime;
boolean _permissaototal = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 26;BA.debugLine="Activity.LoadLayout(\"Layout\")";
parent.mostCurrent._activity.LoadLayout("Layout",mostCurrent.activityBA);
 //BA.debugLineNum = 28;BA.debugLine="lblCepText.Text = \"\"";
parent.mostCurrent._lblceptext.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 30;BA.debugLine="If FirstTime Then";
if (true) break;

case 1:
//if
this.state = 8;
if (_firsttime) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 32;BA.debugLine="DisableStrictMode";
_disablestrictmode();
 //BA.debugLineNum = 34;BA.debugLine="Wait For(SolicitarPermissoes) Complete (Permissa";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _solicitarpermissoes());
this.state = 9;
return;
case 9:
//C
this.state = 4;
_permissaototal = (Boolean) result[0];
;
 //BA.debugLineNum = 36;BA.debugLine="If PermissaoTotal = False Then";
if (true) break;

case 4:
//if
this.state = 7;
if (_permissaototal==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 37;BA.debugLine="MsgboxAsync( \"AppCEP Precisa de permissões espe";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("AppCEP Precisa de permissões especificas para continuar "),BA.ObjectToCharSequence("Ops !"),processBA);
 //BA.debugLineNum = 38;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 39;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 if (true) break;

case 7:
//C
this.state = 8;
;
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(boolean _permissaototal) throws Exception{
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 47;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _bt0_click() throws Exception{
 //BA.debugLineNum = 214;BA.debugLine="Sub bt0_Click";
 //BA.debugLineNum = 215;BA.debugLine="formata( \"0\" )";
_formata("0");
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
public static String  _bt1_click() throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub bt1_Click";
 //BA.debugLineNum = 227;BA.debugLine="formata( \"1\" )";
_formata("1");
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return "";
}
public static String  _bt2_click() throws Exception{
 //BA.debugLineNum = 222;BA.debugLine="Sub bt2_Click";
 //BA.debugLineNum = 223;BA.debugLine="formata( \"2\" )";
_formata("2");
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public static String  _bt3_click() throws Exception{
 //BA.debugLineNum = 218;BA.debugLine="Sub bt3_Click";
 //BA.debugLineNum = 219;BA.debugLine="formata( \"3\" )";
_formata("3");
 //BA.debugLineNum = 220;BA.debugLine="End Sub";
return "";
}
public static String  _bt4_click() throws Exception{
 //BA.debugLineNum = 238;BA.debugLine="Sub bt4_Click";
 //BA.debugLineNum = 239;BA.debugLine="formata( \"4\" )";
_formata("4");
 //BA.debugLineNum = 240;BA.debugLine="End Sub";
return "";
}
public static String  _bt5_click() throws Exception{
 //BA.debugLineNum = 234;BA.debugLine="Sub bt5_Click";
 //BA.debugLineNum = 235;BA.debugLine="formata( \"5\" )";
_formata("5");
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return "";
}
public static String  _bt6_click() throws Exception{
 //BA.debugLineNum = 230;BA.debugLine="Sub bt6_Click";
 //BA.debugLineNum = 231;BA.debugLine="formata( \"6\" )";
_formata("6");
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
return "";
}
public static String  _bt7_click() throws Exception{
 //BA.debugLineNum = 250;BA.debugLine="Sub bt7_Click";
 //BA.debugLineNum = 251;BA.debugLine="formata( \"7\" )";
_formata("7");
 //BA.debugLineNum = 252;BA.debugLine="End Sub";
return "";
}
public static String  _bt8_click() throws Exception{
 //BA.debugLineNum = 246;BA.debugLine="Sub bt8_Click";
 //BA.debugLineNum = 247;BA.debugLine="formata( \"8\" )";
_formata("8");
 //BA.debugLineNum = 248;BA.debugLine="End Sub";
return "";
}
public static String  _bt9_click() throws Exception{
 //BA.debugLineNum = 242;BA.debugLine="Sub bt9_Click";
 //BA.debugLineNum = 243;BA.debugLine="formata( \"9\" )";
_formata("9");
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
return "";
}
public static String  _btbs_click() throws Exception{
 //BA.debugLineNum = 255;BA.debugLine="Sub btbs_Click";
 //BA.debugLineNum = 256;BA.debugLine="If lblCepText.Text.Length > 0 Then";
if (mostCurrent._lblceptext.getText().length()>0) { 
 //BA.debugLineNum = 257;BA.debugLine="lblCepText.Text = lblCepText.Text.SubString2( 0,";
mostCurrent._lblceptext.setText(BA.ObjectToCharSequence(mostCurrent._lblceptext.getText().substring((int) (0),(int) (mostCurrent._lblceptext.getText().length()-1))));
 };
 //BA.debugLineNum = 259;BA.debugLine="End Sub";
return "";
}
public static String  _disablestrictmode() throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4j.object.JavaObject _policy = null;
anywheresoftware.b4j.object.JavaObject _sm = null;
 //BA.debugLineNum = 159;BA.debugLine="Sub DisableStrictMode";
 //BA.debugLineNum = 160;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 161;BA.debugLine="jo.InitializeStatic(\"android.os.Build.VERSION\")";
_jo.InitializeStatic("android.os.Build.VERSION");
 //BA.debugLineNum = 162;BA.debugLine="If jo.GetField(\"SDK_INT\") > 9 Then";
if ((double)(BA.ObjectToNumber(_jo.GetField("SDK_INT")))>9) { 
 //BA.debugLineNum = 163;BA.debugLine="Dim policy As JavaObject";
_policy = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 164;BA.debugLine="policy = policy.InitializeNewInstance(\"android.o";
_policy = _policy.InitializeNewInstance("android.os.StrictMode.ThreadPolicy.Builder",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 165;BA.debugLine="policy = policy.RunMethodJO(\"permitAll\", Null).R";
_policy = _policy.RunMethodJO("permitAll",(Object[])(anywheresoftware.b4a.keywords.Common.Null)).RunMethodJO("build",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 166;BA.debugLine="Dim sm As JavaObject";
_sm = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 167;BA.debugLine="sm.InitializeStatic(\"android.os.StrictMode\").Run";
_sm.InitializeStatic("android.os.StrictMode").RunMethod("setThreadPolicy",new Object[]{(Object)(_policy.getObject())});
 };
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _formata(String _cnumero) throws Exception{
String _c = "";
 //BA.debugLineNum = 262;BA.debugLine="Sub formata( cnumero As String )";
 //BA.debugLineNum = 264;BA.debugLine="lblCepText.Text = lblCepText.Text & cnumero";
mostCurrent._lblceptext.setText(BA.ObjectToCharSequence(mostCurrent._lblceptext.getText()+_cnumero));
 //BA.debugLineNum = 267;BA.debugLine="Dim c As String";
_c = "";
 //BA.debugLineNum = 269;BA.debugLine="c = lblCepText.Text.Replace( \".\", \"\" ).Replace( \"";
_c = mostCurrent._lblceptext.getText().replace(".","").replace("-","");
 //BA.debugLineNum = 271;BA.debugLine="Try";
try { //BA.debugLineNum = 272;BA.debugLine="c = c.SubString2( 0,2 ) & \".\" & c.SubString2(2,5";
_c = _c.substring((int) (0),(int) (2))+"."+_c.substring((int) (2),(int) (5))+"-"+_c.substring((int) (5),(int) (8));
 //BA.debugLineNum = 274;BA.debugLine="lblCepText.Text = c";
mostCurrent._lblceptext.setText(BA.ObjectToCharSequence(_c));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); };
 //BA.debugLineNum = 280;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 21;BA.debugLine="Private lblCepText As Label";
mostCurrent._lblceptext = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _lbllimpar_click() throws Exception{
 //BA.debugLineNum = 152;BA.debugLine="Sub lblLimpar_Click";
 //BA.debugLineNum = 155;BA.debugLine="lblCepText.Text = \"\"";
mostCurrent._lblceptext.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 156;BA.debugLine="End Sub";
return "";
}
public static void  _papesquisar_click() throws Exception{
ResumableSub_paPesquisar_Click rsub = new ResumableSub_paPesquisar_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_paPesquisar_Click extends BA.ResumableSub {
public ResumableSub_paPesquisar_Click(appCep.tssistemas.main parent) {
this.parent = parent;
}
appCep.tssistemas.main parent;
String _url = "";
appCep.tssistemas.httpjob _j = null;
String _res = "";
anywheresoftware.b4a.objects.collections.JSONParser _jsp = null;
anywheresoftware.b4a.objects.collections.Map _m = null;
Object _obj = null;
String _cep = "";
int _result = 0;
anywheresoftware.b4a.objects.IntentWrapper _mapintent = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
try {

        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 54;BA.debugLine="If lblCepText.Text.trim.Length < 8 Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent.mostCurrent._lblceptext.getText().trim().length()<8) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 55;BA.debugLine="MsgboxAsync( \"Cep tem que pelo menos 8 numeros\",";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Cep tem que pelo menos 8 numeros"),BA.ObjectToCharSequence("ops"),processBA);
 //BA.debugLineNum = 56;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 4:
//C
this.state = 5;
;
 //BA.debugLineNum = 61;BA.debugLine="ProgressDialogShow( \"Aguarde ...\"  )";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Aguarde ..."));
 //BA.debugLineNum = 63;BA.debugLine="Try";
if (true) break;

case 5:
//try
this.state = 34;
this.catchState = 33;
this.state = 7;
if (true) break;

case 7:
//C
this.state = 8;
this.catchState = 33;
 //BA.debugLineNum = 65;BA.debugLine="Dim url As String = \"http://viacep.com.br/ws/\" &";
_url = "http://viacep.com.br/ws/"+parent.mostCurrent._lblceptext.getText().replace(".","").replace("-","")+"/json/";
 //BA.debugLineNum = 66;BA.debugLine="Dim j As HttpJob";
_j = new appCep.tssistemas.httpjob();
 //BA.debugLineNum = 68;BA.debugLine="j.Initialize( \"\", Me )";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 69;BA.debugLine="j.Download( url )";
_j._download /*String*/ (_url);
 //BA.debugLineNum = 70;BA.debugLine="Wait For (j) JobDone (j  As HttpJob )";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 35;
return;
case 35:
//C
this.state = 8;
_j = (appCep.tssistemas.httpjob) result[0];
;
 //BA.debugLineNum = 72;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 36;
return;
case 36:
//C
this.state = 8;
;
 //BA.debugLineNum = 74;BA.debugLine="If j.Success Then";
if (true) break;

case 8:
//if
this.state = 31;
if (_j._success /*boolean*/ ) { 
this.state = 10;
}if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 76;BA.debugLine="Dim res As String";
_res = "";
 //BA.debugLineNum = 77;BA.debugLine="res = j.GetString";
_res = _j._getstring /*String*/ ();
 //BA.debugLineNum = 79;BA.debugLine="Dim jsp As JSONParser";
_jsp = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 80;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 83;BA.debugLine="jsp.Initialize(res)";
_jsp.Initialize(_res);
 //BA.debugLineNum = 84;BA.debugLine="Dim obj As Object= jsp.NextObject";
_obj = (Object)(_jsp.NextObject().getObject());
 //BA.debugLineNum = 86;BA.debugLine="If obj Is Map Then";
if (true) break;

case 11:
//if
this.state = 30;
if (_obj instanceof anywheresoftware.b4a.objects.collections.Map.MyMap) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 88;BA.debugLine="m  = obj";
_m = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(_obj));
 //BA.debugLineNum = 90;BA.debugLine="Dim cep As String";
_cep = "";
 //BA.debugLineNum = 92;BA.debugLine="cep = m.Get( \"logradouro\" ) & CRLF & _";
_cep = BA.ObjectToString(_m.Get((Object)("logradouro")))+anywheresoftware.b4a.keywords.Common.CRLF+BA.ObjectToString(_m.Get((Object)("complemento")))+anywheresoftware.b4a.keywords.Common.CRLF+"BAIRRO : "+BA.ObjectToString(_m.Get((Object)("bairro")))+anywheresoftware.b4a.keywords.Common.CRLF+"CIDADE : "+BA.ObjectToString(_m.Get((Object)("localidade")))+anywheresoftware.b4a.keywords.Common.CRLF+"UF : "+BA.ObjectToString(_m.Get((Object)("uf")))+anywheresoftware.b4a.keywords.Common.CRLF+"IBGE : "+BA.ObjectToString(_m.Get((Object)("ibge")));
 //BA.debugLineNum = 100;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 102;BA.debugLine="If cep.LastIndexOf( \"null\" )   > 0 Then";
if (true) break;

case 14:
//if
this.state = 29;
if (_cep.lastIndexOf("null")>0) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 29;
 //BA.debugLineNum = 103;BA.debugLine="MsgboxAsync( \"CEP não encontrado !\", \"Desculp";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("CEP não encontrado !"),BA.ObjectToCharSequence("Desculpe!"),processBA);
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 107;BA.debugLine="Msgbox2Async(cep, \"Achei !\", \"Abrir Mapa\", \"\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence(_cep),BA.ObjectToCharSequence("Achei !"),"Abrir Mapa","","Continuar",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 108;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 37;
return;
case 37:
//C
this.state = 19;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 109;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 19:
//if
this.state = 28;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 112;BA.debugLine="Dim MapIntent As Intent";
_mapintent = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 113;BA.debugLine="MapIntent.Initialize(MapIntent.ACTION_VIEW,\"";
_mapintent.Initialize(_mapintent.ACTION_VIEW,"geo:0,0?q='"+BA.ObjectToString(_m.Get((Object)("logradouro")))+"','"+BA.ObjectToString(_m.Get((Object)("localidade")))+"','"+BA.ObjectToString(_m.Get((Object)("uf")))+"'");
 //BA.debugLineNum = 114;BA.debugLine="MapIntent.SetComponent(\"googlemaps\")";
_mapintent.SetComponent("googlemaps");
 //BA.debugLineNum = 116;BA.debugLine="Try";
if (true) break;

case 22:
//try
this.state = 27;
this.catchState = 26;
this.state = 24;
if (true) break;

case 24:
//C
this.state = 27;
this.catchState = 26;
 //BA.debugLineNum = 117;BA.debugLine="StartActivity(MapIntent)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_mapintent.getObject()));
 if (true) break;

case 26:
//C
this.state = 27;
this.catchState = 33;
 //BA.debugLineNum = 119;BA.debugLine="MsgboxAsync( \"GoogleMaps não encontrado !\",";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("GoogleMaps não encontrado !"),BA.ObjectToCharSequence("Ops !"),processBA);
 if (true) break;
if (true) break;

case 27:
//C
this.state = 28;
this.catchState = 33;
;
 if (true) break;

case 28:
//C
this.state = 29;
;
 if (true) break;

case 29:
//C
this.state = 30;
;
 if (true) break;

case 30:
//C
this.state = 31;
;
 if (true) break;

case 31:
//C
this.state = 34;
;
 //BA.debugLineNum = 132;BA.debugLine="j.Release";
_j._release /*String*/ ();
 //BA.debugLineNum = 134;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 33:
//C
this.state = 34;
this.catchState = 0;
 //BA.debugLineNum = 139;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 141;BA.debugLine="Log( LastException.Message )";
anywheresoftware.b4a.keywords.Common.LogImpl("4917595",anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),0);
 //BA.debugLineNum = 143;BA.debugLine="MsgboxAsync(\"Erro inexperado, tente novamente !\"";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Erro inexperado, tente novamente !"),BA.ObjectToCharSequence("Ops!"),processBA);
 if (true) break;
if (true) break;

case 34:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
if (true) break;
}} 
       catch (Exception e0) {
			
if (catchState == 0)
    throw e0;
else {
    state = catchState;
processBA.setLastException(e0);}
            }
        }
    }
}
public static void  _jobdone(appCep.tssistemas.httpjob _j) throws Exception{
}
public static void  _msgbox_result(int _result) throws Exception{
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
httputils2service._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _solicitarpermissoes() throws Exception{
ResumableSub_SolicitarPermissoes rsub = new ResumableSub_SolicitarPermissoes(null);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_SolicitarPermissoes extends BA.ResumableSub {
public ResumableSub_SolicitarPermissoes(appCep.tssistemas.main parent) {
this.parent = parent;
}
appCep.tssistemas.main parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
{
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = -1;
 //BA.debugLineNum = 173;BA.debugLine="Starter.ncount = 0";
parent.mostCurrent._starter._ncount /*int*/  = (int) (0);
 //BA.debugLineNum = 209;BA.debugLine="Return ( Starter.nCount = 0 )";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)((parent.mostCurrent._starter._ncount /*int*/ ==0)));return;};
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
}
