/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.7  Built on : Nov 20, 2017 (11:41:50 GMT)
 */
package tier2.webserver;


/**
 *  ExtensionMapper class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ExtensionMapper {
    public static java.lang.Object getTypeObject(
        java.lang.String namespaceURI, java.lang.String typeName,
        javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
        if ("http://common/xsd".equals(namespaceURI) && "Car".equals(typeName)) {
            return common.xsd.Car.Factory.parse(reader);
        }

        if ("http://common/xsd".equals(namespaceURI) &&
                "CarPart".equals(typeName)) {
            return common.xsd.CarPart.Factory.parse(reader);
        }

        if ("http://common/xsd".equals(namespaceURI) &&
                "Pallet".equals(typeName)) {
            return common.xsd.Pallet.Factory.parse(reader);
        }

        if ("http://common/xsd".equals(namespaceURI) &&
                "Product".equals(typeName)) {
            return common.xsd.Product.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}
