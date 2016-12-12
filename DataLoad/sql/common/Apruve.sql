insert into policy( policy_id, policyname, policytype_id,storeent_id, properties ) values((select max(policy_id) + 1 from policy ) , 'Apruve', 'Payment ',  -1, 'attrPageName=StandardApruve&paymentConfigurationId=default&display=true&compatibleMode=false');

insert into policydesc(policy_id, language_id, Description,longDescription ) values((select policy_id from policy  where policyname = 'Apruve'), -1, 'Apruve', 'Apruve');

insert into policycmd (policy_id, businesscmdclass) values ((select policy_id  from policy  where policyname = 'Apruve'), 'com.ibm.commerce.payment.actions.commands.DoPaymentActionsPolicyCmdImpl') ;
insert into policycmd (policy_id, businesscmdclass) values ((select policy_id  from policy  where policyname = 'Apruve'), 'com.ibm.commerce.payment.actions.commands.EditPaymentInstructionPolicyCmdImpl');
insert into policycmd (policy_id, businesscmdclass) values ((select policy_id  from policy  where policyname = 'Apruve'), 'com.ibm.commerce.payment.actions.commands.QueryPaymentsInfoPolicyCmdImpl');