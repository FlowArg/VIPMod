var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var MethodNode = Java.type('org.objectweb.asm.tree.MethodNode');
var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var InvokeDynamicInsnNode = Java.type('org.objectweb.asm.tree.InvokeDynamicInsnNode');
var Handle = Java.type('org.objectweb.asm.Handle');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

function initializeCoreMod() {
    return {
        'ProcessLoadingTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/resources/model/ModelBakery',
                'methodName': 'm_119306_',
                'methodDesc': '(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V'
            },
            'transformer': function(mn) {
                var f = new LabelNode();
                var g = new LabelNode();

                mn.instructions.clear();
                mn.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "fr/flowarg/vip3/utils/ModelLoadingOptimizer", "ITEMS_LOCATION", "Ljava/util/List;"));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/resources/model/ModelResourceLocation", "toString", "()Ljava/lang/String;"));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "contains", "(Ljava/lang/Object;)Z"));
                mn.instructions.add(new JumpInsnNode(Opcodes.IFEQ, f));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/flowarg/vip3/utils/ModelLoadingOptimizer", "createGeneratedItemModel", "(Lnet/minecraft/resources/ResourceLocation;)Lorg/apache/commons/lang3/tuple/Pair;"));
                mn.instructions.add(new VarInsnNode(Opcodes.ASTORE, 3));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "org/apache/commons/lang3/tuple/Pair", "getRight", "()Ljava/lang/Object;"));
                mn.instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/client/resources/model/UnbakedModel"));
                mn.instructions.add(new VarInsnNode(Opcodes.ASTORE, 2));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/model/ModelBakery", ASMAPI.mapField("f_119212_"), "Ljava/util/Map;"));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "org/apache/commons/lang3/tuple/Pair", "getLeft", "()Ljava/lang/Object;"));
                mn.instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/resources/ResourceLocation"));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
                mn.instructions.add(new InsnNode(Opcodes.POP));
                mn.instructions.add(new JumpInsnNode(Opcodes.GOTO, g));
                mn.instructions.add(f);
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/resources/model/ModelBakery", ASMAPI.mapMethod("m_119341_"), "(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/resources/model/UnbakedModel;"));
                mn.instructions.add(new VarInsnNode(Opcodes.ASTORE, 2));
                mn.instructions.add(g);
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/model/ModelBakery", ASMAPI.mapField("f_119212_"), "Ljava/util/Map;"));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
                mn.instructions.add(new InsnNode(Opcodes.POP));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/model/ModelBakery", ASMAPI.mapField("f_119214_"), "Ljava/util/Map;"));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
                mn.instructions.add(new InsnNode(Opcodes.POP));
                mn.instructions.add(new InsnNode(Opcodes.RETURN));
                return mn;
            }
        }
    }
}
