var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// https://github.com/UltimateBoomer/mc-smoothboot/blob/1.18/src/main/java/io/github/ultimateboomer/smoothboot/mixin/client/IntegratedServerMixin.java
function initializeCoreMod() {
    return {
        'MinecraftTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/server/IntegratedServer',
                'methodName': '<init>',
                'methodDesc': '(Ljava/lang/Thread;Lnet/minecraft/client/Minecraft;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/server/WorldStem;Lcom/mojang/authlib/minecraft/MinecraftSessionService;Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/server/players/GameProfileCache;Lnet/minecraft/server/level/progress/ChunkProgressListenerFactory;)V'
            },
            'transformer': function (mn) {
                var insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;"));
                insnList.add(new InsnNode(Opcodes.ICONST_5));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "setPriority", "(I)V"));
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "fr/flowarg/vip3/VIP3", "LOGGER", "Lorg/apache/logging/log4j/Logger;"));
                insnList.add(new LdcInsnNode("Initialized integrated server thread"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "debug", "(Ljava/lang/String;)V"));
                insnList.add(new InsnNode(Opcodes.RETURN));

                for(var i = 0; i < mn.instructions.toArray().length; i++) {
                    if(mn.instructions.toArray()[i].getOpcode() == Opcodes.RETURN) {
                        mn.instructions.insertBefore(mn.instructions.toArray()[i], insnList);
                        break;
                    }
                }
                return mn;
            }
        }
    }
}