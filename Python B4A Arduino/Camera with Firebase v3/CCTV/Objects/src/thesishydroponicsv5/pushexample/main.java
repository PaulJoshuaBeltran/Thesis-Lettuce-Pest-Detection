package thesishydroponicsv5.pushexample;


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
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "thesishydroponicsv5.pushexample", "thesishydroponicsv5.pushexample.main");
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
		activityBA = new BA(this, layout, processBA, "thesishydroponicsv5.pushexample", "thesishydroponicsv5.pushexample.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "thesishydroponicsv5.pushexample.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create " + (isFirst ? "(first time)" : "") + " **");
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
public static anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public static anywheresoftware.b4a.objects.Timer _tmr = null;
public static boolean _willtakepic = false;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtname = null;
public thesishydroponicsv5.pushexample.cameraexclass _camex = null;
public static int _intervalms = 0;
public static long _lastpreviewsaved = 0L;
public static String _bucket = "";
public thesishydroponicsv5.pushexample.starter _starter = null;
public thesishydroponicsv5.pushexample.firebasemessaging _firebasemessaging = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 36;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 37;BA.debugLine="Activity.LoadLayout(\"1\")";
mostCurrent._activity.LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 38;BA.debugLine="Log($\"My ip: ${Starter.manager.MyIp}\"$)";
anywheresoftware.b4a.keywords.Common.LogImpl("1131074",("My ip: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(mostCurrent._starter._manager /*thesishydroponicsv5.pushexample.servermanager*/ ._getmyip /*String*/ ()))+""),0);
 //BA.debugLineNum = 39;BA.debugLine="tmr.Initialize(\"MyTimer\", 1000)";
_tmr.Initialize(processBA,"MyTimer",(long) (1000));
 //BA.debugLineNum = 40;BA.debugLine="tmr.Enabled = True";
_tmr.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 57;BA.debugLine="If camEx.IsInitialized Then	camEx.Release";
if (mostCurrent._camex.IsInitialized /*boolean*/ ()) { 
mostCurrent._camex._release /*String*/ ();};
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(thesishydroponicsv5.pushexample.main parent) {
this.parent = parent;
}
thesishydroponicsv5.pushexample.main parent;
String _permission = "";
boolean _result = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 44;BA.debugLine="rp.CheckAndRequest(rp.PERMISSION_CAMERA)";
parent._rp.CheckAndRequest(processBA,parent._rp.PERMISSION_CAMERA);
 //BA.debugLineNum = 45;BA.debugLine="Wait For Activity_PermissionResult (Permission As";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, this, null);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 46;BA.debugLine="If Result Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_result) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 47;BA.debugLine="InitializeCamera";
_initializecamera();
 //BA.debugLineNum = 48;BA.debugLine="UpdateUI";
_updateui();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_permissionresult(String _permission,boolean _result) throws Exception{
}
public static void  _btndownloadpublic_click() throws Exception{
ResumableSub_btnDownloadPublic_Click rsub = new ResumableSub_btnDownloadPublic_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnDownloadPublic_Click extends BA.ResumableSub {
public ResumableSub_btnDownloadPublic_Click(thesishydroponicsv5.pushexample.main parent) {
this.parent = parent;
}
thesishydroponicsv5.pushexample.main parent;
anywheresoftware.b4x.objects.FirebaseStorageWrapper _storage = null;
String _serverpath = "";
boolean _success = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 127;BA.debugLine="Dim storage As FirebaseStorage = CreateFirebaseSt";
_storage = _createfirebasestorage();
 //BA.debugLineNum = 128;BA.debugLine="storage.DownloadFile(\"/public/Untitled.png\", File";
_storage.DownloadFile(processBA,"/public/Untitled.png",anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"out.png");
 //BA.debugLineNum = 129;BA.debugLine="Wait For (storage) Storage_DownloadCompleted (Ser";
anywheresoftware.b4a.keywords.Common.WaitFor("storage_downloadcompleted", processBA, this, (Object)(_storage));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_serverpath = (String) result[0];
_success = (Boolean) result[1];
;
 //BA.debugLineNum = 130;BA.debugLine="ToastMessageShow($\"DownloadCompleted. Success = $";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(("DownloadCompleted. Success = "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+"")),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 131;BA.debugLine="If Not(Success) Then Log(LastException)";
if (true) break;

case 1:
//if
this.state = 6;
if (anywheresoftware.b4a.keywords.Common.Not(_success)) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
anywheresoftware.b4a.keywords.Common.LogImpl("1720902",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _storage_downloadcompleted(String _serverpath,boolean _success) throws Exception{
}
public static void  _btnuploadauth_click() throws Exception{
ResumableSub_btnUploadAuth_Click rsub = new ResumableSub_btnUploadAuth_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnUploadAuth_Click extends BA.ResumableSub {
public ResumableSub_btnUploadAuth_Click(thesishydroponicsv5.pushexample.main parent) {
this.parent = parent;
}
thesishydroponicsv5.pushexample.main parent;
anywheresoftware.b4x.objects.FirebaseStorageWrapper _storage = null;
String _serverpath = "";
boolean _success = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 135;BA.debugLine="Dim storage As FirebaseStorage = CreateFirebaseSt";
_storage = _createfirebasestorage();
 //BA.debugLineNum = 136;BA.debugLine="storage.UploadFile(File.DirInternal, \"out.png\", \"";
_storage.UploadFile(processBA,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"out.png","/public/Untitled1.png");
 //BA.debugLineNum = 137;BA.debugLine="Wait For (storage) Storage_UploadCompleted (Serve";
anywheresoftware.b4a.keywords.Common.WaitFor("storage_uploadcompleted", processBA, this, (Object)(_storage));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_serverpath = (String) result[0];
_success = (Boolean) result[1];
;
 //BA.debugLineNum = 138;BA.debugLine="ToastMessageShow($\"UploadCompleted. Success = ${S";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(("UploadCompleted. Success = "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+"")),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 139;BA.debugLine="If Not(Success) Then Log(LastException)";
if (true) break;

case 1:
//if
this.state = 6;
if (anywheresoftware.b4a.keywords.Common.Not(_success)) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
anywheresoftware.b4a.keywords.Common.LogImpl("1786437",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _storage_uploadcompleted(String _serverpath,boolean _success) throws Exception{
}
public static String  _btnuploaduser_click() throws Exception{
 //BA.debugLineNum = 142;BA.debugLine="Sub btnUploadUser_Click";
 //BA.debugLineNum = 143;BA.debugLine="Try";
try { //BA.debugLineNum = 144;BA.debugLine="File.WriteString(File.DirInternal, \"android.txt\"";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"android.txt","CONTENT 69");
 //BA.debugLineNum = 145;BA.debugLine="ToastMessageShow(File.DirInternal & \" || IT WORK";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.File.getDirInternal()+" || IT WORKED, HALLE-FUCKING-LUJAH"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="ToastMessageShow(File.ReadString(File.DirInterna";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"android.txt")),anywheresoftware.b4a.keywords.Common.True);
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 148;BA.debugLine="ToastMessageShow(\"WORK FOR FUCK SAKE AGAIN\", Tru";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("WORK FOR FUCK SAKE AGAIN"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
return "";
}
public static void  _camera1_picturetaken(byte[] _data) throws Exception{
ResumableSub_Camera1_PictureTaken rsub = new ResumableSub_Camera1_PictureTaken(null,_data);
rsub.resume(processBA, null);
}
public static class ResumableSub_Camera1_PictureTaken extends BA.ResumableSub {
public ResumableSub_Camera1_PictureTaken(thesishydroponicsv5.pushexample.main parent,byte[] _data) {
this.parent = parent;
this._data = _data;
}
thesishydroponicsv5.pushexample.main parent;
byte[] _data;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
anywheresoftware.b4x.objects.FirebaseStorageWrapper _storage = null;
String _serverpath = "";
boolean _success = false;

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
 //BA.debugLineNum = 89;BA.debugLine="camEx.StartPreview";
parent.mostCurrent._camex._startpreview /*String*/ ();
 //BA.debugLineNum = 94;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 96;BA.debugLine="out = File.OpenOutput(File.DirInternal, txtName.t";
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),parent.mostCurrent._txtname.getText()+".jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 97;BA.debugLine="out.WriteBytes(Data, 0, Data.Length)";
_out.WriteBytes(_data,(int) (0),_data.length);
 //BA.debugLineNum = 98;BA.debugLine="out.Close";
_out.Close();
 //BA.debugLineNum = 100;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 13;
this.catchState = 12;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 12;
 //BA.debugLineNum = 101;BA.debugLine="Dim storage As FirebaseStorage = CreateFirebaseS";
_storage = _createfirebasestorage();
 //BA.debugLineNum = 102;BA.debugLine="storage.UploadFile(File.DirInternal, txtName.tex";
_storage.UploadFile(processBA,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),parent.mostCurrent._txtname.getText()+".jpg","/public/android.png");
 //BA.debugLineNum = 103;BA.debugLine="Wait For (storage) Storage_UploadCompleted (Serv";
anywheresoftware.b4a.keywords.Common.WaitFor("storage_uploadcompleted", processBA, this, (Object)(_storage));
this.state = 14;
return;
case 14:
//C
this.state = 4;
_serverpath = (String) result[0];
_success = (Boolean) result[1];
;
 //BA.debugLineNum = 104;BA.debugLine="ToastMessageShow($\"UploadCompleted. Success = ${";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(("UploadCompleted. Success = "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+"")),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 105;BA.debugLine="If Success Then";
if (true) break;

case 4:
//if
this.state = 7;
if (_success) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 106;BA.debugLine="ToastMessageShow(\"UPLOAD PIC SUCCESSFUL\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("UPLOAD PIC SUCCESSFUL"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 107;BA.debugLine="Log(\"UPLOAD PIC SUCCESSFUL\")";
anywheresoftware.b4a.keywords.Common.LogImpl("1589843","UPLOAD PIC SUCCESSFUL",0);
 if (true) break;
;
 //BA.debugLineNum = 109;BA.debugLine="If Not(Success) Then";

case 7:
//if
this.state = 10;
if (anywheresoftware.b4a.keywords.Common.Not(_success)) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 110;BA.debugLine="ToastMessageShow(\"UPLOAD PIC UN-SUCCESSFUL\", Tr";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("UPLOAD PIC UN-SUCCESSFUL"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 111;BA.debugLine="Log(\"UPLOAD PIC UN-SUCCESSFUL\")";
anywheresoftware.b4a.keywords.Common.LogImpl("1589847","UPLOAD PIC UN-SUCCESSFUL",0);
 if (true) break;

case 10:
//C
this.state = 13;
;
 if (true) break;

case 12:
//C
this.state = 13;
this.catchState = 0;
 //BA.debugLineNum = 114;BA.debugLine="ToastMessageShow(\"UPLOAD PIC UN-SUCCESSFUL\", Tru";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("UPLOAD PIC UN-SUCCESSFUL"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 115;BA.debugLine="Log(\"UPLOAD PIC UN-SUCCESSFUL\")";
anywheresoftware.b4a.keywords.Common.LogImpl("1589851","UPLOAD PIC UN-SUCCESSFUL",0);
 if (true) break;
if (true) break;

case 13:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 117;BA.debugLine="ToastMessageShow(\"Picture saved\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Picture saved"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
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
public static String  _camera1_preview(byte[] _previewpic) throws Exception{
byte[] _jpeg = null;
 //BA.debugLineNum = 65;BA.debugLine="Sub Camera1_Preview (PreviewPic() As Byte)";
 //BA.debugLineNum = 66;BA.debugLine="If DateTime.Now > lastPreviewSaved + IntervalMs T";
if (anywheresoftware.b4a.keywords.Common.DateTime.getNow()>_lastpreviewsaved+_intervalms) { 
 //BA.debugLineNum = 67;BA.debugLine="Dim jpeg() As Byte = camEx.PreviewImageToJpeg(Pr";
_jpeg = mostCurrent._camex._previewimagetojpeg /*byte[]*/ (_previewpic,(int) (70));
 //BA.debugLineNum = 68;BA.debugLine="lastPreviewSaved = DateTime.Now";
_lastpreviewsaved = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 69;BA.debugLine="Starter.manager.NewBitmap(jpeg)";
mostCurrent._starter._manager /*thesishydroponicsv5.pushexample.servermanager*/ ._newbitmap /*String*/ (_jpeg);
 };
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _camera1_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 73;BA.debugLine="Sub Camera1_Ready (Success As Boolean)";
 //BA.debugLineNum = 74;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 75;BA.debugLine="camEx.SetJpegQuality(90)";
mostCurrent._camex._setjpegquality /*String*/ ((int) (90));
 //BA.debugLineNum = 76;BA.debugLine="camEx.SetContinuousAutoFocus";
mostCurrent._camex._setcontinuousautofocus /*String*/ ();
 //BA.debugLineNum = 80;BA.debugLine="camEx.SetPreviewSize(1440, 1080)";
mostCurrent._camex._setpreviewsize /*String*/ ((int) (1440),(int) (1080));
 //BA.debugLineNum = 81;BA.debugLine="camEx.CommitParameters";
mostCurrent._camex._commitparameters /*String*/ ();
 //BA.debugLineNum = 82;BA.debugLine="camEx.StartPreview";
mostCurrent._camex._startpreview /*String*/ ();
 }else {
 //BA.debugLineNum = 84;BA.debugLine="ToastMessageShow(\"Cannot open camera.\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Cannot open camera."),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4x.objects.FirebaseStorageWrapper  _createfirebasestorage() throws Exception{
anywheresoftware.b4x.objects.FirebaseStorageWrapper _storage = null;
 //BA.debugLineNum = 154;BA.debugLine="Sub CreateFirebaseStorage As FirebaseStorage";
 //BA.debugLineNum = 155;BA.debugLine="Dim storage As FirebaseStorage";
_storage = new anywheresoftware.b4x.objects.FirebaseStorageWrapper();
 //BA.debugLineNum = 156;BA.debugLine="storage.Initialize(\"storage\", bucket)";
_storage.Initialize("storage",mostCurrent._bucket);
 //BA.debugLineNum = 157;BA.debugLine="Return storage";
if (true) return _storage;
 //BA.debugLineNum = 158;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 27;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private txtName As EditText";
mostCurrent._txtname = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private camEx As CameraExClass";
mostCurrent._camex = new thesishydroponicsv5.pushexample.cameraexclass();
 //BA.debugLineNum = 31;BA.debugLine="Private IntervalMs As Int = 150";
_intervalms = (int) (150);
 //BA.debugLineNum = 32;BA.debugLine="Private lastPreviewSaved As Long";
_lastpreviewsaved = 0L;
 //BA.debugLineNum = 33;BA.debugLine="Private bucket As String = \"gs://thesis-hydroponi";
mostCurrent._bucket = "gs://thesis-hydroponics-v5.appspot.com";
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public static String  _initializecamera() throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Private Sub InitializeCamera";
 //BA.debugLineNum = 53;BA.debugLine="camEx.Initialize(Panel1, False, Me, \"Camera1\")";
mostCurrent._camex._initialize /*String*/ (mostCurrent.activityBA,mostCurrent._panel1,anywheresoftware.b4a.keywords.Common.False,main.getObject(),"Camera1");
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static void  _mytimer_tick() throws Exception{
ResumableSub_MyTimer_Tick rsub = new ResumableSub_MyTimer_Tick(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_MyTimer_Tick extends BA.ResumableSub {
public ResumableSub_MyTimer_Tick(thesishydroponicsv5.pushexample.main parent) {
this.parent = parent;
}
thesishydroponicsv5.pushexample.main parent;
String _availability = "";
anywheresoftware.b4x.objects.FirebaseStorageWrapper _storage = null;
String _serverpath = "";
boolean _success = false;

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
 //BA.debugLineNum = 161;BA.debugLine="Dim availability As String";
_availability = "";
 //BA.debugLineNum = 162;BA.debugLine="Dim storage As FirebaseStorage = CreateFirebaseSt";
_storage = _createfirebasestorage();
 //BA.debugLineNum = 164;BA.debugLine="If willTakePic == True Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._willtakepic==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 165;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 9;
this.catchState = 8;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 9;
this.catchState = 8;
 //BA.debugLineNum = 166;BA.debugLine="camEx.TakePicture";
parent.mostCurrent._camex._takepicture /*String*/ ();
 //BA.debugLineNum = 167;BA.debugLine="ToastMessageShow(\"TAKE PIC SUCCESSFUL\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("TAKE PIC SUCCESSFUL"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 168;BA.debugLine="Log(\"TAKE PIC SUCCESSFUL\")";
anywheresoftware.b4a.keywords.Common.LogImpl("1983048","TAKE PIC SUCCESSFUL",0);
 //BA.debugLineNum = 169;BA.debugLine="willTakePic = False";
parent._willtakepic = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 170;BA.debugLine="availability = \"IS_AVAIL\"";
_availability = "IS_AVAIL";
 if (true) break;

case 8:
//C
this.state = 9;
this.catchState = 0;
 //BA.debugLineNum = 172;BA.debugLine="ToastMessageShow(\"TAKE PIC UN-SUCCESSFUL\", True";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("TAKE PIC UN-SUCCESSFUL"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 173;BA.debugLine="Log(\"TAKE PIC UN-SUCCESSFUL\")";
anywheresoftware.b4a.keywords.Common.LogImpl("1983053","TAKE PIC UN-SUCCESSFUL",0);
 //BA.debugLineNum = 174;BA.debugLine="availability = \"IS_UNAVAIL\"";
_availability = "IS_UNAVAIL";
 if (true) break;
if (true) break;

case 9:
//C
this.state = 10;
this.catchState = 0;
;
 //BA.debugLineNum = 178;BA.debugLine="File.writestring(File.dirinternal, \"android.txt\"";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"android.txt",_availability);
 //BA.debugLineNum = 179;BA.debugLine="storage.uploadfile(File.dirinternal, \"android.tx";
_storage.UploadFile(processBA,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"android.txt","/public/android.txt");
 //BA.debugLineNum = 180;BA.debugLine="wait for (storage) storage_uploadcompleted (serv";
anywheresoftware.b4a.keywords.Common.WaitFor("storage_uploadcompleted", processBA, this, (Object)(_storage));
this.state = 11;
return;
case 11:
//C
this.state = 10;
_serverpath = (String) result[0];
_success = (Boolean) result[1];
;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 217;BA.debugLine="End Sub";
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

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
firebasemessaging._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 21;BA.debugLine="Private rp As RuntimePermissions";
_rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 22;BA.debugLine="Dim tmr As Timer";
_tmr = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 23;BA.debugLine="Dim willTakePic As Boolean = False";
_willtakepic = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _takepic_click() throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Sub takepic_Click";
 //BA.debugLineNum = 121;BA.debugLine="ToastMessageShow(\"Take Camera Picture\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Take Camera Picture"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 122;BA.debugLine="camEx.TakePicture";
mostCurrent._camex._takepicture /*String*/ ();
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _updateui() throws Exception{
 //BA.debugLineNum = 60;BA.debugLine="Public Sub UpdateUI";
 //BA.debugLineNum = 61;BA.debugLine="Label1.Text = $\"My ip: ${Starter.manager.MyIp} Nu";
mostCurrent._label1.setText(BA.ObjectToCharSequence(("My ip: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(mostCurrent._starter._manager /*thesishydroponicsv5.pushexample.servermanager*/ ._getmyip /*String*/ ()))+"\n"+"Number of clients: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(mostCurrent._starter._manager /*thesishydroponicsv5.pushexample.servermanager*/ ._getnumberofclients /*int*/ ()))+"")));
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
}
