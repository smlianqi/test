// automatically generated by the FlatBuffers compiler, do not modify

package com.elex.im.message.flat;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class ChatRoomMessageInfo extends Table {
  public static ChatRoomMessageInfo getRootAsChatRoomMessageInfo(ByteBuffer _bb) { return getRootAsChatRoomMessageInfo(_bb, new ChatRoomMessageInfo()); }
  public static ChatRoomMessageInfo getRootAsChatRoomMessageInfo(ByteBuffer _bb, ChatRoomMessageInfo obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public ChatRoomMessageInfo __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public ChatRoomInfo chatRoom() { return chatRoom(new ChatRoomInfo()); }
  public ChatRoomInfo chatRoom(ChatRoomInfo obj) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(o + bb_pos), bb) : null; }
  public ChatRoomMemberInfo self() { return self(new ChatRoomMemberInfo()); }
  public ChatRoomMemberInfo self(ChatRoomMemberInfo obj) { int o = __offset(6); return o != 0 ? obj.__assign(__indirect(o + bb_pos), bb) : null; }
  public ChatMessageInfo chatMessage(int j) { return chatMessage(new ChatMessageInfo(), j); }
  public ChatMessageInfo chatMessage(ChatMessageInfo obj, int j) { int o = __offset(8); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int chatMessageLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public long lastOrder() { int o = __offset(10); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }
  public ChatUserInfo users(int j) { return users(new ChatUserInfo(), j); }
  public ChatUserInfo users(ChatUserInfo obj, int j) { int o = __offset(12); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int usersLength() { int o = __offset(12); return o != 0 ? __vector_len(o) : 0; }
  public long roomType() { int o = __offset(14); return o != 0 ? (long)bb.getInt(o + bb_pos) & 0xFFFFFFFFL : 0L; }

  public static int createChatRoomMessageInfo(FlatBufferBuilder builder,
      int chatRoomOffset,
      int selfOffset,
      int chatMessageOffset,
      long lastOrder,
      int usersOffset,
      long roomType) {
    builder.startObject(6);
    ChatRoomMessageInfo.addLastOrder(builder, lastOrder);
    ChatRoomMessageInfo.addRoomType(builder, roomType);
    ChatRoomMessageInfo.addUsers(builder, usersOffset);
    ChatRoomMessageInfo.addChatMessage(builder, chatMessageOffset);
    ChatRoomMessageInfo.addSelf(builder, selfOffset);
    ChatRoomMessageInfo.addChatRoom(builder, chatRoomOffset);
    return ChatRoomMessageInfo.endChatRoomMessageInfo(builder);
  }

  public static void startChatRoomMessageInfo(FlatBufferBuilder builder) { builder.startObject(6); }
  public static void addChatRoom(FlatBufferBuilder builder, int chatRoomOffset) { builder.addOffset(0, chatRoomOffset, 0); }
  public static void addSelf(FlatBufferBuilder builder, int selfOffset) { builder.addOffset(1, selfOffset, 0); }
  public static void addChatMessage(FlatBufferBuilder builder, int chatMessageOffset) { builder.addOffset(2, chatMessageOffset, 0); }
  public static int createChatMessageVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startChatMessageVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addLastOrder(FlatBufferBuilder builder, long lastOrder) { builder.addLong(3, lastOrder, 0L); }
  public static void addUsers(FlatBufferBuilder builder, int usersOffset) { builder.addOffset(4, usersOffset, 0); }
  public static int createUsersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startUsersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addRoomType(FlatBufferBuilder builder, long roomType) { builder.addInt(5, (int)roomType, (int)0L); }
  public static int endChatRoomMessageInfo(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

