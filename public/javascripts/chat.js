/**
 * Code for the WebSocket chat application.
 */

const urlParams = new URLSearchParams(location.search);
let user = "anonymous"
 for (const [key, value] of urlParams) {
     console.log(`${key}:${value}`);
     if (key == "user") {
        user = value;
        console.log(`The session user is ${user} .......` );
     }
 }

const roomInputField = document.getElementById("room-input");
const roomOutputArea = document.getElementById("room-area");
const roomChatSocketRoute = "ws://localhost:9000/roomChatSocket/"+user
const roomChatSocket = new WebSocket(roomChatSocketRoute);

roomInputField.onkeydown = (event) => {
    if(event.key === 'Enter') {
        roomChatSocket.send(roomInputField.value);
        roomInputField.value = '';
    }
}

roomChatSocket.onopen = (event) => roomChatSocket.send("New user connected to room.");
roomChatSocket.onmessage = (event) => {
    roomOutputArea.value += '\n Room Message: ' + event.data;
}

const whisperToField = document.getElementById("whisper-user");
const whisperInputField = document.getElementById("whisper-input");
const whisperOutputArea = document.getElementById("whisper-area");
const whisperSocketRoute = "ws://localhost:9000/whisperSocket/"+user
const whisperSocket = new WebSocket(whisperSocketRoute);

whisperSocket.onopen = (event) => console.log("Whisper inbox ready");
whisperSocket.onmessage = (event) => {
    whisperOutputArea.value  += '\n' + event.data;
    console.log("Received Whisper: "+ event.data);
}

whisperInputField.onkeydown = (event) => {
    if(event.key === 'Enter') {
        let whisperMsg =  { to: whisperToField.value, msg: whisperInputField.value };
        whisperSocket.send(JSON.stringify(whisperMsg));
        whisperInputField.value = '';
    }
}
