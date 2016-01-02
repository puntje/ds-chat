/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package avro.chat.proto;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface ChatClientServer {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"ChatClientServer\",\"namespace\":\"avro.chat.proto\",\"types\":[],\"messages\":{\"isAlive\":{\"request\":[],\"response\":\"null\"},\"inPrivateRoom\":{\"request\":[],\"response\":\"boolean\"},\"incomingMessage\":{\"request\":[{\"name\":\"message\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"null\"},\"sendPrivateMessage\":{\"request\":[{\"name\":\"senderName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"null\"},\"video\":{\"request\":[{\"name\":\"mode\",\"type\":\"boolean\"}],\"response\":\"null\"},\"isAwaitingVideo\":{\"request\":[],\"response\":\"boolean\"},\"register\":{\"request\":[{\"name\":\"privateName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"privateAddress\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"boolean\"},\"leave\":{\"request\":[{\"name\":\"closeOtherProxy\",\"type\":\"boolean\"}],\"response\":\"null\"}}}");
  java.lang.Void isAlive() throws org.apache.avro.AvroRemoteException;
  boolean inPrivateRoom() throws org.apache.avro.AvroRemoteException;
  java.lang.Void incomingMessage(java.lang.String message) throws org.apache.avro.AvroRemoteException;
  java.lang.Void sendPrivateMessage(java.lang.String senderName) throws org.apache.avro.AvroRemoteException;
  java.lang.Void video(boolean mode) throws org.apache.avro.AvroRemoteException;
  boolean isAwaitingVideo() throws org.apache.avro.AvroRemoteException;
  boolean register(java.lang.String privateName, java.lang.String privateAddress) throws org.apache.avro.AvroRemoteException;
  java.lang.Void leave(boolean closeOtherProxy) throws org.apache.avro.AvroRemoteException;

  @SuppressWarnings("all")
  public interface Callback extends ChatClientServer {
    public static final org.apache.avro.Protocol PROTOCOL = avro.chat.proto.ChatClientServer.PROTOCOL;
    void isAlive(org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
    void inPrivateRoom(org.apache.avro.ipc.Callback<java.lang.Boolean> callback) throws java.io.IOException;
    void incomingMessage(java.lang.String message, org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
    void sendPrivateMessage(java.lang.String senderName, org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
    void video(boolean mode, org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
    void isAwaitingVideo(org.apache.avro.ipc.Callback<java.lang.Boolean> callback) throws java.io.IOException;
    void register(java.lang.String privateName, java.lang.String privateAddress, org.apache.avro.ipc.Callback<java.lang.Boolean> callback) throws java.io.IOException;
    void leave(boolean closeOtherProxy, org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
  }
}