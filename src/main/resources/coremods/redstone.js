var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var FieldNode = Java.type('org.objectweb.asm.tree.FieldNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// https://github.com/SpaceWalkerRS/alternate-current
function initializeCoreMod() {
    return {
        'ServerLevelTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/server/level/ServerLevel'
            },
            'transformer': function (classNode) {
                classNode.interfaces.add("fr/flowarg/vip3/utils/extensions/IServerLevel");
        		classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "wireHandler", "Lfr/flowarg/vip3/utils/wire/WireHandler;", null, null));
        		var mn = new MethodNode();
        		mn.access = Opcodes.ACC_PUBLIC;
        		mn.name = "getWireHandler";
        		mn.desc = "()Lfr/flowarg/vip3/utils/wire/WireHandler;";
        		var insnList = new InsnList();
        		insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        		insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/level/ServerLevel", "wireHandler", "Lfr/flowarg/vip3/utils/wire/WireHandler;"));
        		insnList.add(new InsnNode(Opcodes.ARETURN));
        		mn.instructions = insnList;
        		classNode.methods.add(mn);
                return classNode;
            }
        },
        'ServerLevelConstructorTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/server/level/ServerLevel',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/Holder;Lnet/minecraft/server/level/progress/ChunkProgressListener;Lnet/minecraft/world/level/chunk/ChunkGenerator;ZJLjava/util/List;Z)V'
            },
            'transformer': function (mn) {
                var list = new InsnList();
        	    list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        		list.add(new TypeInsnNode(Opcodes.NEW, "fr/flowarg/vip3/utils/wire/WireHandler"));
        		list.add(new InsnNode(Opcodes.DUP));
        		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        		list.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/server/level/ServerLevel"));
        		list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "fr/flowarg/vip3/utils/wire/WireHandler", "<init>", "(Lnet/minecraft/server/level/ServerLevel;)V"));
        		list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/server/level/ServerLevel", "wireHandler", "Lfr/flowarg/vip3/utils/wire/WireHandler;"));

        		for(var j = 0; j < mn.instructions.toArray().length; j++)
        		{
        			var insn = mn.instructions.toArray()[j];
        			if(insn.getOpcode() == Opcodes.RETURN)
        			{
        				mn.instructions.insertBefore(insn, list);
        				break;
        			}
        		}
                return mn;
            }
        },
        'UpdatePowerStrengthTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/world/level/block/RedStoneWireBlock',
                'methodName': 'm_55530_',
                'methodDesc': '(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V'
            },
            'transformer': function (mn) {
                mn.instructions.insert(new InsnNode(Opcodes.RETURN));
                return mn;
            }
        },
        'OnPlaceTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/world/level/block/RedStoneWireBlock',
                'methodName': 'm_6807_',
                'methodDesc': '(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V'
            },
            'transformer': function (mn) {
                for(var i = 0; i < mn.instructions.toArray().length; i++)
                {
                    var insn = mn.instructions.toArray()[i];
                    if(insn.getOpcode() == Opcodes.INVOKEVIRTUAL)
                    {
                        if(insn.name == ASMAPI.mapMethod('m_55530_'))
                        {
                            var insnList = new InsnList();
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            insnList.add(new TypeInsnNode(Opcodes.CHECKCAST, "fr/flowarg/vip3/utils/extensions/IServerLevel"));
                            insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "fr/flowarg/vip3/utils/extensions/IServerLevel", "getWireHandler", "()Lfr/flowarg/vip3/utils/wire/WireHandler;", true));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                            insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "fr/flowarg/vip3/utils/wire/WireHandler", "onWireAdded", "(Lnet/minecraft/core/BlockPos;)V", false));
                            mn.instructions.insertBefore(insn, insnList);
                            break;
                        }
                    }
                }
                return mn;
            }
        },
        'OnRemoveTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/world/level/block/RedStoneWireBlock',
                'methodName': 'm_6810_',
                'methodDesc': '(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V'
            },
            'transformer': function (mn) {
                for(var i = 0; i < mn.instructions.toArray().length; i++)
                {
                    var insn = mn.instructions.toArray()[i];
                    if(insn.getOpcode() == Opcodes.INVOKEVIRTUAL)
                    {
                        if(insn.name == ASMAPI.mapMethod('m_55530_'))
                        {
                            var insnList = new InsnList();
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            insnList.add(new TypeInsnNode(Opcodes.CHECKCAST, "fr/flowarg/vip3/utils/extensions/IServerLevel"));
                            insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "fr/flowarg/vip3/utils/extensions/IServerLevel", "getWireHandler", "()Lfr/flowarg/vip3/utils/wire/WireHandler;", true));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "fr/flowarg/vip3/utils/wire/WireHandler", "onWireRemoved", "(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", false));
                            mn.instructions.insertBefore(insn, insnList);
                            break;
                        }
                    }
                }
                return mn;
            }
        },
        'NeighborChangedTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/world/level/block/RedStoneWireBlock',
                'methodName': 'm_6861_',
                'methodDesc': '(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;Z)V'
            },
            'transformer': function (mn) {
                var insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                insnList.add(new TypeInsnNode(Opcodes.CHECKCAST, "fr/flowarg/vip3/utils/extensions/IServerLevel"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "fr/flowarg/vip3/utils/extensions/IServerLevel", "getWireHandler", "()Lfr/flowarg/vip3/utils/wire/WireHandler;", true));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "fr/flowarg/vip3/utils/wire/WireHandler", "onWireUpdated", "(Lnet/minecraft/core/BlockPos;)V", false));
                insnList.add(new InsnNode(Opcodes.RETURN));
                mn.instructions.insert(insnList);
                return mn;
            }
        }
    }
}