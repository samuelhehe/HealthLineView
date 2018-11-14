# HealthLineView
基于 williamchartdemo 改的 仿照宝宝知道健康部分，画的自定义view，半成品，有需要可以自己拿去改改。

# 使用方法很简单

```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_below="@+id/appbar"
    tools:context=".MainActivity">


    <com.db.williamchartdemo.health.HealthScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#eeffff"
        android:overScrollMode="never"
        android:scrollbars="none" />

</FrameLayout>

```

