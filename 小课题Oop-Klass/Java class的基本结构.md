[TOC]

## 整理Java class的基本结构，整理Klass的相关数据结构及作用

### **根据 Java 虚拟机规范，一个Class文件由单个 ClassFile 结构组成：**

***

1. 无符号数属于基本的数据类型，以 u1、u2、u4、u8来分别代表 1 个字节、2 个字节、4 个字节和 8个字节的无符号数，无符号数可以用来描述数字、索引引用、数量值或者按照 UTF-8 编码结构构成的字符串值。对于字符串，则使用u1数组进行表示。
2. 表是由多个无符号数或者其他表作为数据项构成的复合数据类型，所有表都习惯性地以「_info」结尾。表用于描述有层次关系的复合结构的数据，整个 Class 文件就是一张ClassFile 表，它由下表中所示的数据项构成。

```scss
ClassFile {
  u4  magic;   //Class 文件的标志，魔术
  u2  minor_version;  //Class 的附版本号
  u2  major_version;  //Class 的主版本号
  u2  constant_pool_count;  //常量池表项的数量
  cp_info  constant_pool[constant_pool_count-1];  //常量池表项，索引为1~constant_pool_count-1
  u2  access_flags;  //Class 的访问标志（类访问修饰符）
  u2  this_class;  //表示当前类的引用
  u2  super_class;  //表示父类的引用
  u2  interfaces_count;  //实现接口数量
  u2  interfaces[interfaces_count];  //接口索引数组
  u2  fields_count;  //此类的字段表中的字段数量
  field_info  fields[fields_count];  //一个类会可以有多个字段，字段表
  u2  methods_count;  //此类的方法表中的方法数量
  method_info  methods[methods_count];  //一个类可以有个多个方法，方法表
  u2  attributes_count;  //此类的属性表中的属性数量
  attribute_info  attributes[attributes_count];  //属性表集合
}
```

![](.\picture\class_structure\1.png)







####  魔数(magic)

​       所有的由Java编译器编译而成的class文件的前4个字节都是`0xCAFEBABE `,它的作用在于：当JVM在尝试加载某个文件到内存中来的时候，会首先判断此class文件有没有JVM认为可以接受的“签名”，即JVM会首先读取文件的前4个字节，判断该4个字节是否是`0xCAFEBABE`，如果是，则JVM会认为可以将此文件当作class文件来加载并使用。Java一直以咖啡为代言，CAFEBABE可以认为是 Cafe Babe，读音上和Cafe Baby很近。所以这个也许就是代表Cafe Baby的意思。

#### 版本号(minor_version,major_version)

```
u2  minor_version;  //Class 的附版本号
u2  major_version;  //Class 的主版本号
```

​		 随着Java本身的发展，Java语言特性和JVM虚拟机也会有相应的更新和增强。目前我们能够用到的JDK版本如：1.5，1.6，1.7，还有现如今最新的1.8。发布新版本的目的在于：在原有的版本上增加新特性和相应的JVM虚拟机的优化。而随着主版本发布的次版本，则是修改相应主版本上出现的bug。我们平时只需要关注主版本就可以了。

​       主版本号和次版本号在class文件中各占两个字节，副版本号占用第5、6两个字节，而主版本号则占用第7，8两个字节。JDK1.0的主版本号为45，以后的每个新主版本都会在原先版本的基础上加1。若现在使用的是JDK1.7编译出来的class文件，则相应的主版本号应该是51,对应的7，8个字节的十六进制的值应该是 0x33。

​       一个 JVM实例只能支持特定范围内的主版本号 （Mi 至Mj） 和 0 至特定范围内 （0 至 m） 的副版本号。假设一个 Class 文件的格式版本号为 V， 仅当**Mi.0 ≤ v ≤ Mj.m**成立时，这个 Class 文件才可以被此 Java 虚拟机支持。不同版本的 Java 虚拟机实现支持的版本号也不同，高版本号的 Java 虚拟机实现可以支持低版本号的 Class 文件，反之则不成立。

​      JVM在加载class文件的时候，会读取出主版本号，然后比较这个class文件的主版本号和JVM本身的版本号，如果JVM本身的版本号 < class文件的版本号，JVM会认为加载不了这个class文件，会抛出我们经常见到的"java.lang.UnsupportedClassVersionError: Bad version number in .class file " **Error 错误**；反之，JVM会认为可以加载此class文件，继续加载此class文件。

| JDK主版本号 | Class主版本号 | 16进制      |
| ----------- | ------------- | ----------- |
| 1.1         | 45.0          | 00 00 00 2D |
| 1.2  | 46.0 | 00 00 00 2E |
| 1.3  | 47.0 | 00 00 00 2F |
| 1.4  | 48.0 | 00 00 00 30 |
| 1.5  | 49.0 | 00 00 00 31 |
| 1.6  | 50.0 | 00 00 00 32 |
| 1.7  | 51.0 | 00 00 00 33 |
| 1.8  | 52.0 | 00 00 00 34 |

#### 常量池计数器(constant_pool_count)

```
u2  constant_pool_count;  //常量池表项的数量
```

​       常量池是class文件中非常重要的结构，它描述着整个class文件的字面量信息。 常量池是由一组constant_pool结构体数组组成的，而数组的大小则由常量池计数器指定。常量池计数器constant_pool_count 的值 =constant_pool表中的成员数+ 1。constant_pool表的索引值只有在大于 0 且小于constant_pool_count时才会被认为是有效的。

#### 常量池数据区(constant_pool[contstant_pool_count-1])

```
cp_info {
  u1   tag;
  u1   info[];
}
```

​       常量池，constant_pool是一种表结构,它包含 Class 文件结构及其子结构中引用的所有字符串常量、 类或接口名、字段名和其它常量。 常量池中的每一项都具备相同的格式特征——第一个字节作为类型标记用于识别该项是哪种类型的常量，称为 “tag byte” 。常量池的索引范围是 1 至constant_pool_count−1。常量池的具体细节我们会稍后讨论。

​		在JDK1.8中有14种常量池项目类型，每一种项目都有特定的表结构，这14种表有一个共同的特点：开始的第一位是一个 u1 类型的标志位 -tag 来标识常量的类型，代表当前这个常量属于哪种常量类型。**常量池tag类型表：**

| 常量类型           | 标志（tag） | 描述              |
| ------------------ | ----------- | ----------------- |
| CONSTANT_utf8_info | 1           | UTF-8编码的字符串 |
| CONSTANT_Integer_info | 3    | 整形字面量         |
| CONSTANT_Float_info   | 4    | 浮点型字面量       |
| CONSTANT_Long_info    | ５   | 长整型字面量       |
| CONSTANT_Double_info  | ６   | 双精度浮点型字面量 |
| CONSTANT_Class_info     | ７   | 类或接口的符号引用 |
| CONSTANT_String_info    | ８   | 字符串类型字面量   |
| CONSTANT_Fieldref_info  | ９   | 字段的符号引用     |
| CONSTANT_Methodref_info | 10   | 类中方法的符号引用 |
| CONSTANT_InterfaceMethodref_info | 11   | 接口中方法的符号引用 |
| CONSTANT_NameAndType_info        | 12   | 字段或方法的符号引用 |
| CONSTANT_MothodType_info         | 16   | 标志方法类型         |
| CONSTANT_MethodHandle_info  | 15   | 表示方法句柄           |
| CONSTANT_InvokeDynamic_info | 18   | 表示一个动态方法调用点 |

  每种常量类型均有自己的表结构，非常繁琐：
  **常量池项目结构表：**



####  访问标志(access_flags)

访问标志，access_flags 是一种掩码标志，用于表示某个类或者接口的访问权限及基础属性。

| 表示名称   | 值     | 含义                                |
| ---------- | ------ | ----------------------------------- |
| ACC_PUBLIC | 0x0001 | 是否为public类型                    |
| ACC_FINAL  | 0x0010 | 是否被声明为final类型，只有类可设置 |
| ACC_SUPER | 0x0020 | 是否允许使用invokespecial字节码指令的新语意，invokespecial指令的语意在JDB1.2之后发生过改变，为了区别这条指令使用哪种语意，JDK1.0.2之后编译出来的类的这个标志都必须为真。 |
| ACC_INTERFACE | 0x0200 | 标识这是一个接口                                             |
| ACC_ABSTRACT  | 0x0400 | 是否为abstract类型，对于接口或者抽象类来说，此标志为真，其它类值为假 |
| ACC_SYNTHETIC | 0x1000 | 标识这个类并非由用户代码产生的                               |
| ACC_ANNOTATION | 0x2000 | 标识这是一个注解 |
| ACC_ENUM       | 0x4000 | 标识这是一个枚举 |



![](.\picture\class_structure\2.png)

#### 类索引(this_class)

```
  u2  this_class;  //表示当前类的引用
  u2  super_class;  //表示父类的引用
  u2  interfaces_count;  //接口数量
  u2  interfaces[interfaces_count];  //接口索引集合
```

​        类索引，this_class的值必须是对constant_pool表中项目的一个有效索引值。constant_pool表在这个索引处的项必须为CONSTANT_Class_info 类型常量，表示这个 Class 文件所定义的类或接口。

#### 父类索引(super_class)

​        父类索引，对于类来说，super_class 的值必须为 0 或者是对constant_pool 表中项目的一个有效索引值。如果它的值不为 0，那 constant_pool 表在这个索引处的项必须为CONSTANT_Class_info 类型常量，表示这个 Class 文件所定义的类的直接父类。当前类的直接父类，以及它所有间接父类的access_flag 中都不能带有ACC_FINAL 标记。对于接口来说，它的Class文件的super_class项的值必须是对constant_pool表中项目的一个有效索引值。constant_pool表在这个索引处的项必须为代表 java.lang.Object 的 CONSTANT_Class_info 类型常量 。如果 Class 文件的 super_class的值为 0，那这个Class文件只可能是定义的是java.lang.Object类，只有它是唯一没有父类的类。

#### 接口计数器(interfaces_count)

​       接口计数器，interfaces_count的值表示当前类或接口的直接父接口数量。

#### 接口信息数据区(interfaces[interfaces_count])

​      接口表，interfaces[]数组中的每个成员的值必须是一个对constant_pool表中项目的一个有效索引值， 它的长度为 interfaces_count。每个成员 interfaces[i] 必须为 CONSTANT_Class_info类型常量，其中 **0 ≤ i <interfaces_count**。在interfaces[]数组中，成员所表示的接口顺序和对应的源代码中给定的接口顺序（从左至右）一样，即interfaces[0]对应的是源代码中最左边的接口。

#### 字段计数器(fields_count)

​     字段计数器，fields_count的值表示当前 Class 文件 fields[]数组的成员个数。 fields[]数组中每一项都是一个field_info结构的数据项，它用于表示该类或接口声明的类字段或者实例字段。

#### 字段信息数据区(fields[fields_count])

​     字段表，fields[]数组中的每个成员都必须是一个fields_info结构的数据项，用于表示当前类或接口中某个字段的完整描述。 fields[]数组描述当前类或接口声明的所有字段，但不包括从父类或父接口继承的部分。

#### 方法计数器(methods_count)

​      方法计数器， methods_count的值表示当前Class 文件 methods[]数组的成员个数。Methods[]数组中每一项都是一个 method_info 结构的数据项。

#### 方法信息数据区(methods[methods_count])

​      方法表，methods[] 数组中的每个成员都必须是一个 method_info 结构的数据项，用于表示当前类或接口中某个方法的完整描述。如果某个method_info 结构的access_flags 项既没有设置 ACC_NATIVE 标志也没有设置ACC_ABSTRACT 标志，那么它所对应的方法体就应当可以被 Java 虚拟机直接从当前类加载，而不需要引用其它类。 method_info结构可以表示类和接口中定义的所有方法，包括实例方法、类方法、实例初始化方法方法和类或接口初始化方法方法 。methods[]数组只描述当前类或接口中声明的方法，不包括从父类或父接口继承的方法。

#### 属性计数器(attributes_count)

​     属性计数器，attributes_count的值表示当前 Class 文件attributes表的成员个数。attributes表中每一项都是一个attribute_info 结构的数据项。

#### NO16.属性信息数据区(attributes[attributes_count])

 属性表，attributes 表的每个项的值必须是attribute_info结构。

  在Java 7 规范里，Class文件结构中的attributes表的项包括下列定义的属性： InnerClasses 、 EnclosingMethod 、 Synthetic 、Signature、SourceFile，SourceDebugExtension 、Deprecated、RuntimeVisibleAnnotations 、RuntimeInvisibleAnnotations以及BootstrapMethods属性。

   对于支持 Class 文件格式版本号为 49.0 或更高的 Java 虚拟机实现，必须正确识别并读取attributes表中的Signature、RuntimeVisibleAnnotations和RuntimeInvisibleAnnotations属性。对于支持Class文件格式版本号为 51.0 或更高的 Java 虚拟机实现，必须正确识别并读取 attributes表中的BootstrapMethods属性。Java 7 规范 要求任一 Java 虚拟机实现可以自动忽略 Class 文件的 attributes表中的若干 （甚至全部） 它不可识别的属性项。任何本规范未定义的属性不能影响Class文件的语义，只能提供附加的描述信息 。

### 描述符

全限定名和非限定名
  Class文件中的类和接口，都是使用全限定名，又被称作Class的二进制名称。例如“com/ikang/JVM/classfile”是这个类的全限定名，仅仅是把类全名中的“.”替换成了“/”而已，为了使连续的多个全限定名之间不产生混淆，在使用时最后一般会加入一个“;”表示全限定名结束。
  非限定名又被称作简单名称，Class文件中的方法、字段、局部变量、形参名称，都是使用简单名称，没有类型和参数修饰，例如这个类中的getK()方法和k字段的简单名称分别是“getK”和“m”。
  非限定名不得包含ASCII字符. ; [ / ，此外方法名称除了特殊方法名称< init >和< clinit >方法之外，它们不能包含ASCII字符<或>，字段名称或接口方法名称可以是< init >或< clinit >，但是没有方法调用指令可以引用< clinit >，只有invokespecial指令可以引用< init >。

  描述符的作用是用来描述字段的数据类型、方法的参数列表（包括数量、类型以及顺序）和返回值类型。

#### 字段描述符

  根据描述符规则基本数据类型(byte、char、doubIc、float, int、loog、shon, boolean)以及代表无返回伯的void类型都用一个大写字符来表示， 而对象类型则用字符L加对象的全限定名来表示,数组则用[
  字段描述符的类型含义表

| 字段描述符 | 类型 | 含义 |
| ---------- | ---- | ---- |
|B|	byte	|基本类型byte|
|C	|char	|基本类型char|
|D|	double|	基本类型double|
|F|	float	|基本类型float|
|I	|int	|基本类型int|
|J|	long	|基本类型long|
|LClassName;|	reference	|对象类型，例如Ljava/lang/Object;|
|S	|short	|基本类型short|
|Z|	boolean	|基本类型boolean|
|[	|reference	|数组类型|

- 对象类型的实例变量的字段描述符是L+类的二进制名称的内部形式。
- 对于数组类型，每一维度将使用一个前置的“[”字符来描述，如一个定义为“java.lang.String[][]”类型的二维数组，将被记录为：“[[Ljava/lang/String；”，一个整型数组“int[]”将被记录为“[I”

#### 方法描述符

  它基于描述符标识字符含义表所示的字符串的类型表示方法， 同时对方法签名的表示做了一些规定。它将函数的参数类型写在一对小括号中， 并在括号右侧给出方法的返回值。比如， 若有如下方法：

```
Object m(int i, double d, Thread t) {… }
```

  则它的方法描述符为：

`(IDLjava/lang/Thread;)Ljava/lang/Object;`

  可以看到， 方法的参数统一列在一对小括号中， “I”表示int ， “D”表示double，`“Ljava/lang/Thread;”`表示Thread对象。小括号右侧的`Ljava/lang/Object`；表示方法的返同值为Object对象类型。





参考：

https://blog.csdn.net/weixin_43767015/article/details/105310047

https://www.cnblogs.com/blwy-zmh/p/11852916.html

https://www.cnblogs.com/blwy-zmh/p/11857953.html
