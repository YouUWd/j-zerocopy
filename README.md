## Java 中的zero copy，非简单的ByteBuffer复用

### 生成一个10M的f1.txt文件
``` shell script
dd if=/dev/zero of=f1.txt bs=1m count=10
```

### 在某种场景下，我们只想让文件系统认为存在一个超大文件在此，但是并不实际写入硬盘，则可以使用 seek。

```shell script
# 实际大小0m，系统显示1000m
dd if=/dev/zero of=f2.txt bs=1m count=0 seek=1000
# 实际大小20m，系统显示1000m
dd if=/dev/zero of=f3.txt bs=1m count=20 seek=1000
```

### TODO
性能测试