var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

/* A more radical adaptation of https://github.com/mjwells2002/ForgetMeChunk */
function initializeCoreMod() {
    return {
        'MinecraftTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/multiplayer/ClientPacketListener',
                'methodName': 'm_194252_',
                'methodDesc': '(Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)V'
            },
            'transformer': function (mn) {
                mn.instructions.clear();
                mn.instructions.add(new InsnNode(Opcodes.RETURN));
                return mn;
            }
        }
    }
}