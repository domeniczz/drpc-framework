## Intro

[Netty](https://netty.io/) is a popular **asynchronous** event-driven framework that's widely used for scalable network applications. It provides the ability to handle both inbound and outbound data through its **channel pipeline**, consisting of handlers.

Below is how pipeline works during a request that comes in and finally a response goes out, step by step:

## Inbound Handlers (Request Coming In)

1. **Channel Initialization**: When a new connection is established, the pipeline initializes with the defined handlers.

2. **Byte to Message Decoder**: The inbound data, usually in bytes, needs to be converted into a more workable format. This can be done through a handler like `ByteToMessageDecoder`, which translates the raw bytes into a specific protocol or data structure.

3. **Custom Inbound Handler**: Your specific logic for processing the request can be implemented here. You might have multiple handlers for different tasks like authentication, validation, business logic processing, etc.

4. **Exception Handling**: If an exception is raised during the processing of the request, you can handle or log it in a specific inbound handler designed for exception handling.

## Outbound Handlers (Response Going Out)

1. **Custom Outbound Handler**: Before the response is sent back to the client, any customization or processing required for the response can be handled here. This might include things like adding specific headers or encoding the response according to the client's needs.

2. **Message to Byte Encoder**: The response object, usually in a high-level language-specific format, needs to be translated back into bytes for transmission over the network. A handler like `MessageToByteEncoder` would typically handle this task.

3. **Write Operation**: After encoding, the response is written to the channel. A `writeAndFlush` operation is typically used to send the data to the connected client.

4. **Channel Closure (Optional)**: Depending on the design, you may close the channel after the response is sent or keep it open for future communication.