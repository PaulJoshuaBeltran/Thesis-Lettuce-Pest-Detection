B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=6.48
@EndOfDesignText@
Sub Class_Globals
	Private astream As AsyncStreams
	Private mManager As ServerManager
End Sub

Public Sub Initialize (socket As Socket, manager As ServerManager)
	Log("New connection")
	mManager = manager
	astream.Initialize(socket.InputStream, socket.OutputStream, "astream")
	Dim s As String = $"HTTP/1.0 200 OK
Cache-Control: no-cache
Pragma: no-cache
Content-Type: multipart/x-mixed-replace; boundary=--myboundary

"$
	astream.Write(s.Replace(CRLF, Starter.EOL).GetBytes("ASCII"))
End Sub

Public Sub NewFrame (header() As Byte, data() As Byte)
	astream.Write(header)
	astream.Write(data)
	astream.Write(Starter.EOL.GetBytes("ASCII"))
End Sub

Private Sub AStream_NewData (Buffer() As Byte)
	Dim s As String = BytesToString(Buffer, 0, Buffer.Length, "ASCII")
	For Each line As String In Regex.Split("\n", s)
		If line.StartsWith("GET") Then
			'filter out favicon calls
			If line.Trim <> "GET / HTTP/1.1" Then
				Log(line)
				astream.Close
			End If
		End If
	Next
End Sub

Private Sub AStream_Terminated
	AStream_Error
End Sub

Private Sub AStream_Error
	Log("Error")
	mManager.ClientDisconnected(Me)
End Sub