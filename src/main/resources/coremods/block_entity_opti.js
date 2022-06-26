var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'SetChangedBlockEntityTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/world/level/block/entity/BlockEntity',
                'methodName': 'm_155232_',
                'methodDesc': '(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V'
            },
            'transformer': function(methodNode) {
                methodNode.instructions.clear();
                methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/Level", ASMAPI.mapMethod("m_151543_"), "(Lnet/minecraft/core/BlockPos;)V"));
                methodNode.instructions.add(new InsnNode(Opcodes.RETURN));
                return methodNode;
            }
        }
    }
}