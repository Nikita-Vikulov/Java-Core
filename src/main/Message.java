package main;

public class Message {
    private String clientId;
    private String serverId;
    private String message;

    public String getClientId() {
        return clientId;
    }
    public String getServerId() {
        return serverId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //public void setInputMessage(String inputMessage) {
      //  this.inputMessage = inputMessage;
    //}
   // public void setOutputMessage(String outputMessage) {
   //     this.outputMessage = outputMessage;
  //  }
}
