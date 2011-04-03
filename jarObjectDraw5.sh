rm ObjectDraw5.jar

jar cvef application.ObjectDraw5 ObjectDraw5.jar -C bin .
jar uvf ObjectDraw5.jar -C src .
jar uvf ObjectDraw5.jar -C images .