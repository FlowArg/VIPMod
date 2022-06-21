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

// https://github.com/SpaceWalkerRS/alternate-current
function initializeCoreMod() {
    return {
        'BlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/Block'
            },
            'transformer': function (cn) {
                cn.interfaces.add("fr/flowarg/vip3/utils/extensions/IBlock");
                return cn;
            }
        },
        'BlockStateTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/state/BlockState'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_60734_"), "()Lnet/minecraft/world/level/block/Block;"));
                insnList.add(new TypeInsnNode(Opcodes.CHECKCAST, "fr/flowarg/vip3/utils/extensions/IBlock"));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_7160_"), "()Lnet/minecraft/world/level/block/state/BlockState;"));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "fr/flowarg/vip3/utils/extensions/IBlock", "isSignalSourceTo", "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", true));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
        		insnList1.add(new VarInsnNode(Opcodes.ALOAD, 0));
        		insnList1.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_60734_"), "()Lnet/minecraft/world/level/block/Block;"));
        		insnList1.add(new TypeInsnNode(Opcodes.CHECKCAST, "fr/flowarg/vip3/utils/extensions/IBlock"));
        		insnList1.add(new VarInsnNode(Opcodes.ALOAD, 1));
        		insnList1.add(new VarInsnNode(Opcodes.ALOAD, 2));
        		insnList1.add(new VarInsnNode(Opcodes.ALOAD, 0));
        		insnList1.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_7160_"), "()Lnet/minecraft/world/level/block/state/BlockState;"));
        		insnList1.add(new VarInsnNode(Opcodes.ALOAD, 3));
        		insnList1.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "fr/flowarg/vip3/utils/extensions/IBlock", "isDirectSignalSourceTo", "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", true));
        		insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                cn.interfaces.add("fr/flowarg/vip3/utils/extensions/IBlockState");
                return cn;
            }
        },
        'TargetBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/TargetBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);
                return cn;
            }
        },
        'SculkSensorBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/SculkSensorBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);
                return cn;
            }
        },
        'DaylightDetectorBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/DaylightDetectorBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);
                return cn;
            }
        },
        'PoweredBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/PoweredBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);
                return cn;
            }
        },
        'BasePressurePlateBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/BasePressurePlateBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
        		insnList1.add(new VarInsnNode(Opcodes.ALOAD, 4));
        		insnList1.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/core/Direction", "UP", "Lnet/minecraft/core/Direction;"));
        		var labelNode1 = new LabelNode();
        		var labelNode2 = new LabelNode();
        		insnList1.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
        		insnList1.add(new InsnNode(Opcodes.ICONST_1));
        		insnList1.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
        		insnList1.add(labelNode1);
        		insnList1.add(new InsnNode(Opcodes.ICONST_0));
        		insnList1.add(labelNode2);
        		insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'LecternBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/LecternBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
        		insnList1.add(new VarInsnNode(Opcodes.ALOAD, 4));
        		insnList1.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/core/Direction", "UP", "Lnet/minecraft/core/Direction;"));
        		var labelNode1 = new LabelNode();
        		var labelNode2 = new LabelNode();
        		insnList1.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
        		insnList1.add(new InsnNode(Opcodes.ICONST_1));
        		insnList1.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
        		insnList1.add(labelNode1);
        		insnList1.add(new InsnNode(Opcodes.ICONST_0));
        		insnList1.add(labelNode2);
        		insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'TrappedChestBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/TrappedChestBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
        		insnList1.add(new VarInsnNode(Opcodes.ALOAD, 4));
        		insnList1.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/core/Direction", "UP", "Lnet/minecraft/core/Direction;"));
        		var labelNode1 = new LabelNode();
        		var labelNode2 = new LabelNode();
        		insnList1.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
        		insnList1.add(new InsnNode(Opcodes.ICONST_1));
        		insnList1.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
        		insnList1.add(labelNode1);
        		insnList1.add(new InsnNode(Opcodes.ICONST_0));
        		insnList1.add(labelNode2);
        		insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'TripWireHookBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/TripWireHookBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList1.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/world/level/block/state/properties/BlockStateProperties", ASMAPI.mapField("f_61374_"),"Lnet/minecraft/world/level/block/state/properties/DirectionProperty;"));
                insnList1.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_61143_"), "(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"));
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 4));
                var labelNode1 = new LabelNode();
                var labelNode2 = new LabelNode();
                insnList1.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
                insnList1.add(new InsnNode(Opcodes.ICONST_1));
                insnList1.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
                insnList1.add(labelNode1);
                insnList1.add(new InsnNode(Opcodes.ICONST_0));
                insnList1.add(labelNode2);
                insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'LightningRodBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/LightningRodBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList1.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/world/level/block/state/properties/BlockStateProperties", ASMAPI.mapField("f_61374_"), "Lnet/minecraft/world/level/block/state/properties/DirectionProperty;"));
                insnList1.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_61143_"), "(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"));
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 4));
                var labelNode1 = new LabelNode();
                var labelNode2 = new LabelNode();
                insnList1.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
                insnList1.add(new InsnNode(Opcodes.ICONST_1));
                insnList1.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
                insnList1.add(labelNode1);
                insnList1.add(new InsnNode(Opcodes.ICONST_0));
                insnList1.add(labelNode2);
                insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'LeverBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/LeverBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList1.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/world/level/block/LeverBlock", ASMAPI.mapMethod("m_53200_"), "(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/core/Direction;"));
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 4));
                var labelNode1 = new LabelNode();
                var labelNode2 = new LabelNode();
                insnList1.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
                insnList1.add(new InsnNode(Opcodes.ICONST_1));
                insnList1.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
                insnList1.add(labelNode1);
                insnList1.add(new InsnNode(Opcodes.ICONST_0));
                insnList1.add(labelNode2);
                insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'ButtonBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/ButtonBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList1.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/world/level/block/LeverBlock", ASMAPI.mapMethod("m_53200_"), "(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/core/Direction;"));
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 4));
                var labelNode1 = new LabelNode();
                var labelNode2 = new LabelNode();
                insnList1.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
                insnList1.add(new InsnNode(Opcodes.ICONST_1));
                insnList1.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
                insnList1.add(labelNode1);
                insnList1.add(new InsnNode(Opcodes.ICONST_0));
                insnList1.add(labelNode2);
                insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'RedstoneWallTorchBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/RedstoneWallTorchBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/world/level/block/state/properties/BlockStateProperties", ASMAPI.mapField("f_61374_"), "Lnet/minecraft/world/level/block/state/properties/DirectionProperty;"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_61143_"), "(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 4));
                var labelNode1 = new LabelNode();
                var labelNode2 = new LabelNode();
                insnList.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, labelNode1));
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
                insnList.add(labelNode1);
                insnList.add(new InsnNode(Opcodes.ICONST_0));
                insnList.add(labelNode2);
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);
                return cn;
            }
        },
        'DiodeBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/DiodeBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/world/level/block/state/properties/BlockStateProperties", ASMAPI.mapField("f_61374_"), "Lnet/minecraft/world/level/block/state/properties/DirectionProperty;"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_61143_"), "(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 4));
                var labelNode1 = new LabelNode();
                var labelNode2 = new LabelNode();
                insnList.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
                insnList.add(labelNode1);
                insnList.add(new InsnNode(Opcodes.ICONST_0));
                insnList.add(labelNode2);
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;
                mn1.instructions = insnList;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'ObserverBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/ObserverBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/world/level/block/state/properties/BlockStateProperties", ASMAPI.mapField("f_61374_"), "Lnet/minecraft/world/level/block/state/properties/DirectionProperty;"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/level/block/state/BlockState", ASMAPI.mapMethod("m_61143_"), "(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 4));
                var labelNode1 = new LabelNode();
                var labelNode2 = new LabelNode();
                insnList.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode1));
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
                insnList.add(labelNode1);
                insnList.add(new InsnNode(Opcodes.ICONST_0));
                insnList.add(labelNode2);
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;
                mn1.instructions = insnList;
                cn.methods.add(mn1);

                return cn;
            }
        },
        'RedstoneTorchBlockTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/world/level/block/RedstoneTorchBlock'
            },
            'transformer': function (cn) {
                var mn = new MethodNode();
                mn.name = "isSignalSourceTo";
                mn.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn.access = Opcodes.ACC_PUBLIC;
                var insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 4));
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/core/Direction", "UP", "Lnet/minecraft/core/Direction;"));
                var labelNode1 = new LabelNode();
                var labelNode2 = new LabelNode();
                insnList.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, labelNode1));
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new JumpInsnNode(Opcodes.GOTO, labelNode2));
                insnList.add(labelNode1);
                insnList.add(new InsnNode(Opcodes.ICONST_0));
                insnList.add(labelNode2);
                insnList.add(new InsnNode(Opcodes.IRETURN));
                mn.instructions = insnList;
                cn.methods.add(mn);

                var mn1 = new MethodNode();
                mn1.name = "isDirectSignalSourceTo";
                mn1.desc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z";
                mn1.access = Opcodes.ACC_PUBLIC;

        		var insnList1 = new InsnList();
                insnList1.add(new VarInsnNode(Opcodes.ALOAD, 4));
                insnList1.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/core/Direction", "DOWN", "Lnet/minecraft/core/Direction;"));
                var labelNode11 = new LabelNode();
                var labelNode12 = new LabelNode();
                insnList1.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode11));
                insnList1.add(new InsnNode(Opcodes.ICONST_1));
                insnList1.add(new JumpInsnNode(Opcodes.GOTO, labelNode12));
                insnList1.add(labelNode11);
                insnList1.add(new InsnNode(Opcodes.ICONST_0));
                insnList1.add(labelNode12);
                insnList1.add(new InsnNode(Opcodes.IRETURN));
                mn1.instructions = insnList1;
                cn.methods.add(mn1);

                return cn;
            }
        }
    }
}