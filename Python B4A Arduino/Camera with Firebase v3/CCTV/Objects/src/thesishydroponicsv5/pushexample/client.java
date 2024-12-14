package thesishydroponicsv5.pushexample;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class client extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "thesishydroponicsv5.pushexample.client");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", thesishydroponicsv5.pushexample.client.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public thesishydroponicsv5.pushexample.servermanager _mmanager = null;
public thesishydroponicsv5.pushexample.main _main = null;
public thesishydroponicsv5.pushexample.starter _starter = null;
public thesishydroponicsv5.pushexample.firebasemessaging _firebasemessaging = null;
public String  _astream_error() throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Private Sub AStream_Error";
 //BA.debugLineNum = 43;BA.debugLine="Log(\"Error\")";
__c.LogImpl("15636097","Error",0);
 //BA.debugLineNum = 44;BA.debugLine="mManager.ClientDisconnected(Me)";
_mmanager._clientdisconnected /*String*/ ((thesishydroponicsv5.pushexample.client)(this));
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public String  _astream_newdata(byte[] _buffer) throws Exception{
String _s = "";
String _line = "";
 //BA.debugLineNum = 25;BA.debugLine="Private Sub AStream_NewData (Buffer() As Byte)";
 //BA.debugLineNum = 26;BA.debugLine="Dim s As String = BytesToString(Buffer, 0, Buffer";
_s = __c.BytesToString(_buffer,(int) (0),_buffer.length,"ASCII");
 //BA.debugLineNum = 27;BA.debugLine="For Each line As String In Regex.Split(\"\\n\", s)";
{
final String[] group2 = __c.Regex.Split("\\n",_s);
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_line = group2[index2];
 //BA.debugLineNum = 28;BA.debugLine="If line.StartsWith(\"GET\") Then";
if (_line.startsWith("GET")) { 
 //BA.debugLineNum = 30;BA.debugLine="If line.Trim <> \"GET / HTTP/1.1\" Then";
if ((_line.trim()).equals("GET / HTTP/1.1") == false) { 
 //BA.debugLineNum = 31;BA.debugLine="Log(line)";
__c.LogImpl("15505030",_line,0);
 //BA.debugLineNum = 32;BA.debugLine="astream.Close";
_astream.Close();
 };
 };
 }
};
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return "";
}
public String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 38;BA.debugLine="Private Sub AStream_Terminated";
 //BA.debugLineNum = 39;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Private astream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 3;BA.debugLine="Private mManager As ServerManager";
_mmanager = new thesishydroponicsv5.pushexample.servermanager();
 //BA.debugLineNum = 4;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.SocketWrapper _socket,thesishydroponicsv5.pushexample.servermanager _manager) throws Exception{
innerInitialize(_ba);
String _s = "";
 //BA.debugLineNum = 6;BA.debugLine="Public Sub Initialize (socket As Socket, manager A";
 //BA.debugLineNum = 7;BA.debugLine="Log(\"New connection\")";
__c.LogImpl("15373953","New connection",0);
 //BA.debugLineNum = 8;BA.debugLine="mManager = manager";
_mmanager = _manager;
 //BA.debugLineNum = 9;BA.debugLine="astream.Initialize(socket.InputStream, socket.Out";
_astream.Initialize(ba,_socket.getInputStream(),_socket.getOutputStream(),"astream");
 //BA.debugLineNum = 10;BA.debugLine="Dim s As String = $\"HTTP/1.0 200 OK Cache-Control";
_s = ("HTTP/1.0 200 OK\n"+"Cache-Control: no-cache\n"+"Pragma: no-cache\n"+"Content-Type: multipart/x-mixed-replace; boundary=--myboundary\n"+"\n"+"");
 //BA.debugLineNum = 16;BA.debugLine="astream.Write(s.Replace(CRLF, Starter.EOL).GetByt";
_astream.Write(_s.replace(__c.CRLF,_starter._eol /*String*/ ).getBytes("ASCII"));
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return "";
}
public String  _newframe(byte[] _header,byte[] _data) throws Exception{
 //BA.debugLineNum = 19;BA.debugLine="Public Sub NewFrame (header() As Byte, data() As B";
 //BA.debugLineNum = 20;BA.debugLine="astream.Write(header)";
_astream.Write(_header);
 //BA.debugLineNum = 21;BA.debugLine="astream.Write(data)";
_astream.Write(_data);
 //BA.debugLineNum = 22;BA.debugLine="astream.Write(Starter.EOL.GetBytes(\"ASCII\"))";
_astream.Write(_starter._eol /*String*/ .getBytes("ASCII"));
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
