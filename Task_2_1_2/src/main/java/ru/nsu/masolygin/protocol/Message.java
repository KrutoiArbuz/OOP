package ru.nsu.masolygin.protocol;

import ru.nsu.masolygin.protocol.payload.Payload;

public record Message(MessageType type, Payload payload) {

}