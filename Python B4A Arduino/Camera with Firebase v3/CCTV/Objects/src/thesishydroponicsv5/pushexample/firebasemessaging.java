package thesishydroponicsv5.pushexample;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class firebasemessaging extends android.app.Service{
	public static class firebasemessaging_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (firebasemessaging) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, firebasemessaging.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, false, BA.class);
		}

	}
    static firebasemessaging mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return firebasemessaging.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "thesishydroponicsv5.pushexample", "thesishydroponicsv5.pushexample.firebasemessaging");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "thesishydroponicsv5.pushexample.firebasemessaging", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (firebasemessaging) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (false) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (firebasemessaging) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (false)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (firebasemessaging) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (false) {
            BA.LogInfo("** Service (firebasemessaging) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (firebasemessaging) Destroy **");
		    processBA.raiseEvent(null, "service_destroy");
            processBA.service = null;
		    mostCurrent = null;
		    processBA.setActivityPaused(true);
            processBA.runHook("ondestroy", this, null);
        }
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.FirebaseNotificationsService.FirebaseMessageWrapper _fm = null;
public thesishydroponicsv5.pushexample.main _main = null;
public thesishydroponicsv5.pushexample.starter _starter = null;
public static void  _checktoken() throws Exception{
ResumableSub_CheckToken rsub = new ResumableSub_CheckToken(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_CheckToken extends BA.ResumableSub {
public ResumableSub_CheckToken(thesishydroponicsv5.pushexample.firebasemessaging parent) {
this.parent = parent;
}
thesishydroponicsv5.pushexample.firebasemessaging parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 23;BA.debugLine="Log(\"CheckToken: \"&fm.Token)";
anywheresoftware.b4a.keywords.Common.LogImpl("15898241","CheckToken: "+parent._fm.getToken(),0);
 //BA.debugLineNum = 24;BA.debugLine="Do While fm.Token = \"\"";
if (true) break;

case 1:
//do while
this.state = 4;
while ((parent._fm.getToken()).equals("")) {
this.state = 3;
if (true) break;
}
if (true) break;

case 3:
//C
this.state = 1;
 //BA.debugLineNum = 25;BA.debugLine="Log(\"Sleep250\")";
anywheresoftware.b4a.keywords.Common.LogImpl("15898243","Sleep250",0);
 //BA.debugLineNum = 26;BA.debugLine="Sleep(250)";
anywheresoftware.b4a.keywords.Common.Sleep(processBA,this,(int) (250));
this.state = 8;
return;
case 8:
//C
this.state = 1;
;
 if (true) break;
;
 //BA.debugLineNum = 28;BA.debugLine="If fm.Token <> \"\" Then";

case 4:
//if
this.state = 7;
if ((parent._fm.getToken()).equals("") == false) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 29;BA.debugLine="Log(\"Token: \"&fm.Token)";
anywheresoftware.b4a.keywords.Common.LogImpl("15898247","Token: "+parent._fm.getToken(),0);
 if (true) break;

case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _fm_messagearrived(anywheresoftware.b4a.objects.FirebaseNotificationsService.RemoteMessageWrapper _message) throws Exception{
anywheresoftware.b4a.objects.NotificationWrapper _n = null;
 //BA.debugLineNum = 39;BA.debugLine="Sub fm_MessageArrived (Message As RemoteMessage)";
 //BA.debugLineNum = 41;BA.debugLine="Log(\"Message arrived\")";
anywheresoftware.b4a.keywords.Common.LogImpl("16029314","Message arrived",0);
 //BA.debugLineNum = 42;BA.debugLine="Log($\"Message data: ${Message.GetData}\"$)";
anywheresoftware.b4a.keywords.Common.LogImpl("16029315",("Message data: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_message.GetData().getObject()))+""),0);
 //BA.debugLineNum = 43;BA.debugLine="Dim n As Notification";
_n = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 44;BA.debugLine="n.Initialize";
_n.Initialize();
 //BA.debugLineNum = 45;BA.debugLine="n.Icon = \"icon\"";
_n.setIcon("icon");
 //BA.debugLineNum = 46;BA.debugLine="n.SetInfo(Message.GetData.Get(\"title\"), Message.G";
_n.SetInfoNew(processBA,BA.ObjectToCharSequence(_message.GetData().Get((Object)("title"))),BA.ObjectToCharSequence(_message.GetData().Get((Object)("body"))),(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 47;BA.debugLine="n.Notify(1)";
_n.Notify((int) (1));
 //BA.debugLineNum = 48;BA.debugLine="ToastMessageShow($\"Message data: ${Message.GetDat";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(("Message data: "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_message.GetData().getObject()))+"")),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 50;BA.debugLine="If Message.GetData.Get(\"body\") == \"TAKE ANDROID M";
if ((_message.GetData().Get((Object)("body"))).equals((Object)("TAKE ANDROID MOBILE PHONE CAM"))) { 
 //BA.debugLineNum = 51;BA.debugLine="Main.willTakePic = True";
mostCurrent._main._willtakepic /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 53;BA.debugLine="Main.willTakePic = False";
mostCurrent._main._willtakepic /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _fm_tokenrefresh(String _token) throws Exception{
 //BA.debugLineNum = 57;BA.debugLine="Sub fm_TokenRefresh (Token As String)";
 //BA.debugLineNum = 58;BA.debugLine="Log(\"Token is :\" & Token)";
anywheresoftware.b4a.keywords.Common.LogImpl("16094849","Token is :"+_token,0);
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Public fm As FirebaseMessaging";
_fm = new anywheresoftware.b4a.objects.FirebaseNotificationsService.FirebaseMessageWrapper();
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static void  _service_create() throws Exception{
ResumableSub_Service_Create rsub = new ResumableSub_Service_Create(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Service_Create extends BA.ResumableSub {
public ResumableSub_Service_Create(thesishydroponicsv5.pushexample.firebasemessaging parent) {
this.parent = parent;
}
thesishydroponicsv5.pushexample.firebasemessaging parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 11;BA.debugLine="fm.Initialize(\"fm\")";
parent._fm.Initialize(processBA,"fm");
 //BA.debugLineNum = 12;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(processBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 13;BA.debugLine="Log(fm.Token)";
anywheresoftware.b4a.keywords.Common.LogImpl("15767171",parent._fm.getToken(),0);
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static void  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
ResumableSub_Service_Start rsub = new ResumableSub_Service_Start(null,_startingintent);
rsub.resume(processBA, null);
}
public static class ResumableSub_Service_Start extends BA.ResumableSub {
public ResumableSub_Service_Start(thesishydroponicsv5.pushexample.firebasemessaging parent,anywheresoftware.b4a.objects.IntentWrapper _startingintent) {
this.parent = parent;
this._startingintent = _startingintent;
}
thesishydroponicsv5.pushexample.firebasemessaging parent;
anywheresoftware.b4a.objects.IntentWrapper _startingintent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 34;BA.debugLine="If StartingIntent.IsInitialized Then fm.HandleInt";
if (true) break;

case 1:
//if
this.state = 6;
if (_startingintent.IsInitialized()) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
parent._fm.HandleIntent((android.content.Intent)(_startingintent.getObject()));
if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 35;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(processBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 36;BA.debugLine="Service.StopAutomaticForeground 'remove if not us";
parent.mostCurrent._service.StopAutomaticForeground();
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _subscribetotopics() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Public Sub SubscribeToTopics";
 //BA.debugLineNum = 18;BA.debugLine="fm.SubscribeToTopic(\"general\") 'you can subscribe";
_fm.SubscribeToTopic("general");
 //BA.debugLineNum = 19;BA.debugLine="CheckToken";
_checktoken();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
}
