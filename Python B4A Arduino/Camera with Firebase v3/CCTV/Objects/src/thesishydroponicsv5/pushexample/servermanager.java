package thesishydroponicsv5.pushexample;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class servermanager extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "thesishydroponicsv5.pushexample.servermanager");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", thesishydroponicsv5.pushexample.servermanager.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.SocketWrapper.ServerSocketWrapper _server = null;
public anywheresoftware.b4a.objects.collections.List _clients = null;
public thesishydroponicsv5.pushexample.main _main = null;
public thesishydroponicsv5.pushexample.starter _starter = null;
public thesishydroponicsv5.pushexample.firebasemessaging _firebasemessaging = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Private server As ServerSocket";
_server = new anywheresoftware.b4a.objects.SocketWrapper.ServerSocketWrapper();
 //BA.debugLineNum = 3;BA.debugLine="Private clients As List";
_clients = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 4;BA.debugLine="End Sub";
return "";
}
public String  _clientdisconnected(thesishydroponicsv5.pushexample.client _client1) throws Exception{
int _i = 0;
 //BA.debugLineNum = 30;BA.debugLine="Public Sub ClientDisconnected (client1 As Client)";
 //BA.debugLineNum = 31;BA.debugLine="Dim i As Int = clients.IndexOf(client1)";
_i = _clients.IndexOf((Object)(_client1));
 //BA.debugLineNum = 32;BA.debugLine="If i > -1 Then clients.RemoveAt(i)";
if (_i>-1) { 
_clients.RemoveAt(_i);};
 //BA.debugLineNum = 33;BA.debugLine="CallSub(Main, \"UpdateUI\")";
__c.CallSubNew(ba,(Object)(_main.getObject()),"UpdateUI");
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public String  _getmyip() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Public Sub getMyIp As String";
 //BA.debugLineNum = 23;BA.debugLine="Return server.GetMyIP";
if (true) return _server.GetMyIP();
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public int  _getnumberofclients() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Public Sub getNumberOfClients As Int";
 //BA.debugLineNum = 27;BA.debugLine="Return clients.Size";
if (true) return _clients.getSize();
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,int _port) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 6;BA.debugLine="Public Sub Initialize (port As Int)";
 //BA.debugLineNum = 7;BA.debugLine="server.Initialize(port, \"server\")";
_server.Initialize(ba,_port,"server");
 //BA.debugLineNum = 8;BA.debugLine="clients.Initialize";
_clients.Initialize();
 //BA.debugLineNum = 9;BA.debugLine="server.Listen";
_server.Listen();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public String  _newbitmap(byte[] _bmp) throws Exception{
String _s = "";
thesishydroponicsv5.pushexample.client _c = null;
 //BA.debugLineNum = 36;BA.debugLine="Public Sub NewBitmap(bmp() As Byte)";
 //BA.debugLineNum = 37;BA.debugLine="Dim s As String = $\"--myboundary Content-Type:ima";
_s = ("--myboundary\n"+"Content-Type:image/jpeg\n"+"Content-Length:"+__c.SmartStringFormatter("",(Object)(_bmp.length))+"\n"+"\n"+"");
 //BA.debugLineNum = 42;BA.debugLine="s = s.Replace(CRLF, Starter.EOL)";
_s = _s.replace(__c.CRLF,_starter._eol /*String*/ );
 //BA.debugLineNum = 43;BA.debugLine="For Each c As Client In clients";
{
final anywheresoftware.b4a.BA.IterableList group3 = _clients;
final int groupLen3 = group3.getSize()
;int index3 = 0;
;
for (; index3 < groupLen3;index3++){
_c = (thesishydroponicsv5.pushexample.client)(group3.Get(index3));
 //BA.debugLineNum = 44;BA.debugLine="c.NewFrame(s.GetBytes(\"ASCII\"), bmp)";
_c._newframe /*String*/ (_s.getBytes("ASCII"),_bmp);
 }
};
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return "";
}
public String  _server_newconnection(boolean _successful,anywheresoftware.b4a.objects.SocketWrapper _newsocket) throws Exception{
thesishydroponicsv5.pushexample.client _c = null;
 //BA.debugLineNum = 12;BA.debugLine="Private Sub Server_NewConnection (Successful As Bo";
 //BA.debugLineNum = 13;BA.debugLine="If Successful Then";
if (_successful) { 
 //BA.debugLineNum = 14;BA.debugLine="Dim c As Client";
_c = new thesishydroponicsv5.pushexample.client();
 //BA.debugLineNum = 15;BA.debugLine="c.Initialize(NewSocket, Me)";
_c._initialize /*String*/ (ba,_newsocket,(thesishydroponicsv5.pushexample.servermanager)(this));
 //BA.debugLineNum = 16;BA.debugLine="clients.Add(c)";
_clients.Add((Object)(_c));
 };
 //BA.debugLineNum = 18;BA.debugLine="server.Listen";
_server.Listen();
 //BA.debugLineNum = 19;BA.debugLine="CallSub(Main, \"UpdateUI\")";
__c.CallSubNew(ba,(Object)(_main.getObject()),"UpdateUI");
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
