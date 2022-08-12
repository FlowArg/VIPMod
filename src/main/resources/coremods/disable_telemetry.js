var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'OnDisconnectClientPacketListener': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/multiplayer/ClientPacketListener',
                'methodName': 'm_7026_',
                'methodDesc': '(Lnet/minecraft/network/chat/Component;)V'
            },
            'transformer': function(methodNode) {
                var instructions = methodNode.instructions.toArray();
                for(var i = 0; i < instructions.length; i++) {
                    var instruction = instructions[i];
                    if(instruction.getOpcode() != Opcodes.INVOKEVIRTUAL)
                        continue;

                    if(instruction.name != ASMAPI.mapMethod("m_193544_"))
                        continue;

                    methodNode.instructions.remove(instructions[i - 2]);
                    methodNode.instructions.remove(instructions[i - 1]);
                    methodNode.instructions.remove(instruction);
                    break;
                }
                return methodNode;
            }
        },
        'HandleLoginClientPacketListener': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/multiplayer/ClientPacketListener',
                'methodName': 'm_5998_',
                'methodDesc': '(Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)V'
            },
            'transformer': function(methodNode) {
                var instructions = methodNode.instructions.toArray();
                for(var i = 0; i < instructions.length; i++) {
                    var instruction = instructions[i];
                    if(instruction.getOpcode() != Opcodes.INVOKEVIRTUAL)
                        continue;

                    if(instruction.name != ASMAPI.mapMethod("m_193545_"))
                        continue;

                    for (var j = 1; j < 7; j++) {
                        methodNode.instructions.remove(instructions[i - j]);
                    }
                    methodNode.instructions.remove(instruction);
                    break;
                }
                return methodNode;
            }
        },
        'HandleCustomPayloadClientPacketListener': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/multiplayer/ClientPacketListener',
                'methodName': 'm_7413_',
                'methodDesc': '(Lnet/minecraft/network/protocol/game/ClientboundCustomPayloadPacket;)V'
            },
            'transformer': function(methodNode) {
                var instructions = methodNode.instructions.toArray();
                for(var i = 0; i < instructions.length; i++) {
                    var instruction = instructions[i];
                    if(instruction.getOpcode() != Opcodes.INVOKEVIRTUAL)
                        continue;

                    if(instruction.name != ASMAPI.mapMethod("m_193561_"))
                        continue;

                    methodNode.instructions.remove(instructions[i - 1]);
                    methodNode.instructions.remove(instructions[i - 2]);
                    methodNode.instructions.remove(instructions[i - 3]);
                    methodNode.instructions.remove(instruction);
                    break;
                }
                return methodNode;
            }
        },
        'ClientPacketListenerTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/client/multiplayer/ClientPacketListener'
            },
            'transformer': function(classNode) {
                var fields = classNode.fields.toArray();
                for (var i = 0; i < fields.length; i++)
                {
                    var field = fields[i];
                    if(field.name != ASMAPI.mapField("f_194191_"))
                        continue;

                    classNode.fields.remove(field);
                    break;
                }

                var methods = classNode.methods.toArray();
                for(var i = 0; i < methods.length; i++)
                {
                    var method = methods[i];
                    if(method.name != "<init>")
                        continue;

                    method.desc = "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/network/Connection;Lcom/mojang/authlib/GameProfile;)V";

                    var instructions = method.instructions.toArray();
                    for(var j = 0; j < instructions.length; j++) {
                        var instruction = instructions[j];
                        if(instruction.getOpcode() != Opcodes.PUTFIELD)
                            continue;

                        if(instruction.name != ASMAPI.mapField("f_194191_"))
                            continue;

                        method.instructions.remove(instructions[j - 2]);
                        method.instructions.remove(instructions[j - 1]);
                        method.instructions.remove(instruction);
                        break;
                    }
                    break;
                }

                return classNode;
            }
        },
        'ClientHandshakeTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/multiplayer/ClientHandshakePacketListenerImpl',
                'methodName': 'm_7056_',
                'methodDesc': '(Lnet/minecraft/network/protocol/login/ClientboundGameProfilePacket;)V'
            },
            'transformer': function(methodNode) {
                var instructions = methodNode.instructions.toArray();
                for(var i = 0; i < instructions.length; i++) {
                    var instruction = instructions[i];
                    if(instruction.getOpcode() != Opcodes.INVOKEVIRTUAL)
                        continue;

                    if(instruction.name != ASMAPI.mapMethod("m_193590_"))
                        continue;
                    methodNode.instructions.remove(instructions[i - 2]);
                    methodNode.instructions.remove(instructions[i - 1]);
                    methodNode.instructions.remove(instruction);

                    instructions[i + 1].desc = "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/network/Connection;Lcom/mojang/authlib/GameProfile;)V";
                    break;
                }
                return methodNode;
            }
        },
        'MinecraftTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/client/Minecraft'
            },
            'transformer': function(classNode) {
                var methods = classNode.methods.toArray();
                for(var i = 0; i < methods.length; i++) {
                    var method = methods[i];
                    if(method.name != ASMAPI.mapMethod("m_193590_"))
                        continue;

                    classNode.methods.remove(method);
                    break;
                }
                return classNode;
            }
        }
    }
}