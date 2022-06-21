var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// https://github.com/UltimateBoomer/mc-smoothboot/blob/1.18/src/main/java/io/github/ultimateboomer/smoothboot/mixin/client/MainMixin.java
function initializeCoreMod() {
    return {
        'ClientMainTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/main/Main',
                'methodName': 'main',
                'methodDesc': '([Ljava/lang/String;)V'
            },
            'transformer': function (mn) {
                var insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;"));
                insnList.add(new InsnNode(Opcodes.ICONST_5));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "setPriority", "(I)V"));
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "fr/flowarg/vip3/VIP3", "LOGGER", "Lorg/apache/logging/log4j/Logger;"));
                insnList.add(new LdcInsnNode("Initialized client game thread"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "debug", "(Ljava/lang/String;)V"));
                mn.instructions.insert(insnList);
                return mn;
            }
        },
        'IntegratedServerMainTransformer': {
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
        },
        'ServerMainTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/server/Main',
                'methodName': 'main',
                'methodDesc': '([Ljava/lang/String;)V'
            },
            'transformer': function (mn) {
                var insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, 'java/lang/Thread', 'currentThread', '()Ljava/lang/Thread;'));
                insnList.add(new InsnNode(Opcodes.ICONST_5));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, 'java/lang/Thread', 'setPriority', '(I)V'));
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, 'fr/flowarg/vip3/VIP3', 'LOGGER', 'Lorg/apache/logging/log4j/Logger;'));
                insnList.add(new LdcInsnNode("Initialized server game thread"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, 'org/apache/logging/log4j/Logger', 'debug', '(Ljava/lang/String;)V'));
                mn.instructions.insert(insnList);
                return mn;
            }
        },
        'UtilTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/Util',
                'methodName': '<clinit>',
                'methodDesc': '()V'
            },
            'transformer': function (mn) {
                for(var i = 0; i < mn.instructions.toArray().length; i++) {
                    var node = mn.instructions.toArray()[i];
                    if(node.getOpcode() == Opcodes.LDC) {
                        mn.instructions.set(mn.instructions.toArray()[i + 1], new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/flowarg/vip3/utils/ExecutorReplacement", "makeExecutor", "(Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;"));
                    }

                    if(node.getOpcode() == Opcodes.INVOKESTATIC) {
                        if(node.name == "makeIoExecutor") {
                            node.owner = "fr/flowarg/vip3/utils/ExecutorReplacement";
                        }
                    }
                }
                return mn;
            }
        }
    }
}