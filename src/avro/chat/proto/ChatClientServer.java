/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package avro.chat.proto;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface ChatClientServer {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"ChatClientServer\",\"namespace\":\"avro.chat.proto\",\"types\":[],\"messages\":{\"isAlive\":{\"request\":[],\"response\":\"boolean\"}}}");
  boolean isAlive() throws org.apache.avro.AvroRemoteException;

  @SuppressWarnings("all")
  public interface Callback extends ChatClientServer {
    public static final org.apache.avro.Protocol PROTOCOL = avro.chat.proto.ChatClientServer.PROTOCOL;
    void isAlive(org.apache.avro.ipc.Callback<java.lang.Boolean> callback) throws java.io.IOException;
  }
}