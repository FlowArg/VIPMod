var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

// https://github.com/astei/lazydfu/blob/25ed4a75947bce9796f90a99b4232f7884c1b54b/src/main/java/me/steinborn/lazydfu/mixin/SchemasMixin.java
function initializeCoreMod() {
    return {
        'DataFixersTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/util/datafix/DataFixers',
                'methodName': 'm_14529_',
                'methodDesc': '()Lcom/mojang/datafixers/DataFixer;'
            },
            'transformer': function (mn) {
                mn.instructions.clear();
                mn.instructions.add(new TypeInsnNode(Opcodes.NEW, 'fr/flowarg/vip3/utils/LazyDataFixerBuilder'));
                mn.instructions.add(new InsnNode(Opcodes.DUP));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/SharedConstants", ASMAPI.mapMethod("m_183709_"), "()Lnet/minecraft/WorldVersion;"));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/WorldVersion", "getWorldVersion", "()I", true));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, 'fr/flowarg/vip3/utils/LazyDataFixerBuilder', '<init>', "(I)V"));
                mn.instructions.add(new VarInsnNode(Opcodes.ASTORE, 0));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/util/datafix/DataFixers", ASMAPI.mapMethod("m_14513_"), "(Lcom/mojang/datafixers/DataFixerBuilder;)V"));
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/Util", ASMAPI.mapMethod("m_183988_"), "()Ljava/util/concurrent/ExecutorService;"));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/mojang/datafixers/DataFixerBuilder", "build", "(Ljava/util/concurrent/Executor;)Lcom/mojang/datafixers/DataFixer;"));
                mn.instructions.add(new InsnNode(Opcodes.ARETURN));
                return mn;
            }
        }
    }
}