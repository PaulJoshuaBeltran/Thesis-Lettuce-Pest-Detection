B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=12.5
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	
#End Region

Sub Process_Globals
	Public fm As FirebaseMessaging
End Sub

Sub Service_Create
	fm.Initialize("fm")
	Sleep(0)
	Log(fm.Token)

End Sub

Public Sub SubscribeToTopics
	fm.SubscribeToTopic("general") 'you can subscribe to more topics
	CheckToken
End Sub

Sub CheckToken
	Log("CheckToken: "&fm.Token)
	Do While fm.Token = ""
		Log("Sleep250")
		Sleep(250)
	Loop
	If fm.Token <> "" Then
		Log("Token: "&fm.Token)
	End If
End Sub

Sub Service_Start (StartingIntent As Intent)
	If StartingIntent.IsInitialized Then fm.HandleIntent(StartingIntent)
	Sleep(0)
	Service.StopAutomaticForeground 'remove if not using B4A v8+.
End Sub

Sub fm_MessageArrived (Message As RemoteMessage)
	'MsgboxAsync("E' arrivata una notifica","")
	Log("Message arrived")
	Log($"Message data: ${Message.GetData}"$)
	Dim n As Notification
	n.Initialize
	n.Icon = "icon"
	n.SetInfo(Message.GetData.Get("title"), Message.GetData.Get("body"), Main)
	n.Notify(1)
	ToastMessageShow($"Message data: ${Message.GetData}"$, True)
	
	If Message.GetData.Get("body") == "TAKE ANDROID MOBILE PHONE CAM" Then
		Main.willTakePic = True
	Else
		Main.willTakePic = False
	End If
End Sub

Sub fm_TokenRefresh (Token As String)
	Log("Token is :" & Token)
End Sub

Sub Service_Destroy

End Sub