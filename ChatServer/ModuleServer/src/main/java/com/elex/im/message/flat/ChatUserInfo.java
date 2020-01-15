// automatically generated by the FlatBuffers compiler, do not modify

package com.elex.im.message.flat;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class ChatUserInfo extends Table {
  public static ChatUserInfo getRootAsChatUserInfo(ByteBuffer _bb) { return getRootAsChatUserInfo(_bb, new ChatUserInfo()); }
  public static ChatUserInfo getRootAsChatUserInfo(ByteBuffer _bb, ChatUserInfo obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public ChatUserInfo __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String uid() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer uidAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public long userType() { int o = __offset(6); return o != 0 ? (long)bb.getInt(o + bb_pos) & 0xFFFFFFFFL : 0L; }
  public String languageType() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer languageTypeAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }

  public static int createChatUserInfo(FlatBufferBuilder builder,
      int uidOffset,
      long userType,
      int languageTypeOffset) {
    builder.startObject(3);
    ChatUserInfo.addLanguageType(builder, languageTypeOffset);
    ChatUserInfo.addUserType(builder, userType);
    ChatUserInfo.addUid(builder, uidOffset);
    return ChatUserInfo.endChatUserInfo(builder);
  }

  public static void startChatUserInfo(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addUid(FlatBufferBuilder builder, int uidOffset) { builder.addOffset(0, uidOffset, 0); }
  public static void addUserType(FlatBufferBuilder builder, long userType) { builder.addInt(1, (int)userType, (int)0L); }
  public static void addLanguageType(FlatBufferBuilder builder, int languageTypeOffset) { builder.addOffset(2, languageTypeOffset, 0); }
  public static int endChatUserInfo(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

