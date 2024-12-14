B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=6.48
@EndOfDesignText@
Sub Class_Globals
	Private server As ServerSocket
	Private clients As List
End Sub

Public Sub Initialize (port As Int)
	server.Initialize(port, "server")
	clients.Initialize
	server.Listen
End Sub

Private Sub Server_NewConnection (Successful As Boolean, NewSocket As Socket)
	If Successful Then
		Dim c As Client
		c.Initialize(NewSocket, Me)
		clients.Add(c)
	End If
	server.Listen
	CallSub(Main, "UpdateUI")
End Sub

Public Sub getMyIp As String
	Return server.GetMyIP
End Sub

Public Sub getNumberOfClients As Int
	Return clients.Size
End Sub

Public Sub ClientDisconnected (client1 As Client)
	Dim i As Int = clients.IndexOf(client1)
	If i > -1 Then clients.RemoveAt(i)
	CallSub(Main, "UpdateUI")
End Sub

Public Sub NewBitmap(bmp() As Byte)
	Dim s As String = $"--myboundary
Content-Type:image/jpeg
Content-Length:${bmp.Length}

"$
	s = s.Replace(CRLF, Starter.EOL)
	For Each c As Client In clients
		c.NewFrame(s.GetBytes("ASCII"), bmp)
	Next
		
End Sub