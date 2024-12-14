B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=6.48
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	#ExcludeFromLibrary: True
#End Region

Sub Process_Globals
	Public manager As ServerManager
	Public EOL As String = Chr(13) & Chr(10)
	Public Phone As Phone
	Public rp As RuntimePermissions
End Sub

Sub Service_Create
	manager.Initialize(51042)
End Sub

Sub Service_Start (StartingIntent As Intent)
	CallSubDelayed(FirebaseMessaging, "SubscribeToTopics")
	Service.StopAutomaticForeground 'Starter service can start in the foreground state in some edge cases.
End Sub

Sub Service_TaskRemoved
	'This event will be raised when the user removes the app from the recent apps list.
End Sub

'Return true to allow the OS default exceptions handler to handle the uncaught exception.
Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	Return True
End Sub

Sub Service_Destroy

End Sub
