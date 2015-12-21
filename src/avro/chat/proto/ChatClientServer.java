/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package avro.chat.proto;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface ChatClientServer {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"ChatClientServer\",\"namespace\":\"avro.chat.proto\",\"types\":[],\"messages\":{\"isAlive\":{\"request\":[],\"response\":\"null\"},\"incomingMessage\":{\"request\":[{\"name\":\"message\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"null\"},\"sendRequest\":{\"request\":[{\"name\":\"message\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"boolean\"},\"sendVideoRequest\":{\"request\":[{\"name\":\"message\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"file\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"boolean\"}}}");
  java.lang.Void isAlive() throws org.apache.avro.AvroRemoteException;
  java.lang.Void incomingMessage(java.lang.String message) throws org.apache.avro.AvroRemoteException;
  boolean sendRequest(java.lang.String message) throws org.apache.avro.AvroRemoteException;
  boolean sendVideoRequest(java.lang.String message, java.lang.String file) throws org.apache.avro.AvroRemoteException;

  @SuppressWarnings("all")
  public interface Callback extends ChatClientServer {
    public static final org.apache.avro.Protocol PROTOCOL = avro.chat.proto.ChatClientServer.PROTOCOL;
    void isAlive(org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
    void incomingMessage(java.lang.String message, org.apache.avro.ipc.Callback<java.lang.Void> callback) throws java.io.IOException;
    void sendRequest(java.lang.String message, org.apache.avro.ipc.Callback<java.lang.Boolean> callback) throws java.io.IOException;
    void sendVideoRequest(java.lang.String message, java.lang.String file, org.apache.avro.ipc.Callback<java.lang.Boolean> callback) throws java.io.IOException;
  }
}