<!-- Set the taglib -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://commerce.ibm.com/base" prefix="wcbase" %>
<%@ taglib uri="http://commerce.ibm.com/foundation" prefix="wcf" %>
<%@ taglib uri="flow.tld" prefix="flow" %>
<%@ include file="../../../Common/EnvironmentSetup.jspf" %>
<c:set var="paymentAreaNumber" value="${WCParam.currentPaymentArea}"/>
<c:if test="${empty paymentAreaNumber}">
	<c:set var="paymentAreaNumber" value="${param.paymentAreaNumber}" />
</c:if>

<c:set var="account" value="${param.account}"/>
<c:set var="piAmount" value="${param.piAmount}"/>
<c:set var="paymentTCId" value="${param.paymentTCId}"/>
<c:set var="buyerOrgDN" value="${param.buyerOrgDN}"/>
<c:if test="${!empty paymentTCId}" >
	<wcbase:useBean id="paymentTCbean" classname="com.ibm.commerce.contract.beans.PaymentTCDataBean">
       <c:set target="${paymentTCbean}" property="dataBeanKeyReferenceNumber" value="${paymentTCId}"/>
    </wcbase:useBean>
    <c:set var="WCAccountId" value="${paymentTCbean.tradingId}" />
    
   	<c:forEach var="pAttrValue" items="${paymentTCbean.PAttrValues}">
	<%-- Remove the scoped variable --%>
       <c:remove var="attribute"/>
       <wcbase:useBean id="attribute" classname="com.ibm.commerce.utf.beans.PAttributeDataBean">
          <c:set target="${attribute}" property="initKey_referenceNumber" value="${pAttrValue.attributeId}"/>
       </wcbase:useBean>
   	   <c:if test="${attribute.name eq 'account'}">
            <c:if test="${!empty pAttrValue.PAttrValue}">
              <c:set var="valueFromPaymentTC" value="true" />
   		      <c:set var="account" value="${pAttrValue.PAttrValue}" />
            </c:if>
            <c:if test="${empty pAttrValue.PAttrValue}">
	          <c:set var="account" value="" />
            </c:if>
       </c:if>
    </c:forEach>
</c:if>
<div class="card_info" id="WC_StandardLOC_div_1_<c:out value='${paymentAreaNumber}'/>">
	<span class="col1">
		<%@ include file="PaymentAmount.jspf"%>	
	</span>
			<input type="hidden" name="account" id="account1_<c:out value='${paymentAreaNumber}'/>" value ="<c:out value='${account}' />" />
			<input type="hidden" name="paymentTCId" value="<c:out value='${paymentTCId}'/>" id="paymentTCId"/>
			<input type="hidden" name="BuyerOrgDN" id="BuyerOrgDN" value="<c:out value='${buyerOrgDN}'/>" />
			<input type="hidden" name="WCAccountId" id="WCAccountId" value="<c:out value='${WCAccountId}'/>" />
			<input type="hidden" id="mandatoryFields_<c:out value='${paymentAreaNumber}'/>" name="mandatoryFields"  value="billing_address_id"/>
</div>