CREATE TABLE IF NOT EXISTS domain
(
    id SERIAL PRIMARY KEY,
    "createTime" timestamp without time zone,
    "domainName" character varying(255),
    "postmasterAddressId" bigint,
    status integer,
    "updateTime" timestamp without time zone,
    CONSTRAINT uk_4qyl85kb0l95n82ouqir44d0x UNIQUE ("domainName")
);

CREATE TABLE IF NOT EXISTS address
(
    id SERIAL PRIMARY KEY,
    "createTime" timestamp without time zone,
    "displayName" character varying(100),
    "emailAddress" character varying(400),
    endpoint character varying(255),
    status integer,
    type character varying(64) ,
    "updateTime" timestamp without time zone,
    "domainId" bigint NOT NULL,
    CONSTRAINT fk2euw2caoptxa2x1unxx28myos FOREIGN KEY ("domainId")
        REFERENCES domain (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS anchor
(
    id SERIAL PRIMARY KEY,
    "certificateId" bigint NOT NULL,
    "createTime" timestamp without time zone,
    "certificateData" bytea,
    "forIncoming" boolean,
    "forOutgoing" boolean,
    owner character varying(255),
    status integer,
    thumbprint character varying(255),
    "validEndDate" timestamp without time zone,
    "validStartDate" timestamp without time zone
);

CREATE TABLE IF NOT EXISTS certificate
(
    id SERIAL PRIMARY KEY,
    "createTime" timestamp without time zone,
    "certificateData" bytea,
    owner character varying(255),
    "privateKey" boolean,
    status integer,
    thumbprint character varying(255),
    "validEndDate" timestamp without time zone,
    "validStartDate" timestamp without time zone
);

CREATE TABLE IF NOT EXISTS certpolicy
(
    id SERIAL PRIMARY KEY,
    "createTime" timestamp without time zone NOT NULL,
    lexicon integer NOT NULL,
    data bytea NOT NULL,
    "policyName" character varying(255),
    CONSTRAINT uk_gxnmqpsot5r835vgl888kgul8 UNIQUE ("policyName")
);

CREATE TABLE IF NOT EXISTS certpolicygroup
(
    id SERIAL PRIMARY KEY,
    "createTime" timestamp without time zone NOT NULL,
    "policyGroupName" character varying(255),
    CONSTRAINT uk_c749eoa4ewcyqou5270tj7r04 UNIQUE ("policyGroupName")
);

CREATE TABLE IF NOT EXISTS certpolicygroupdomainreltn
(
    id SERIAL PRIMARY KEY,
    policy_group_id bigint NOT NULL,
    domain_id bigint NOT NULL,
    CONSTRAINT fkan8pme5q1h9q7sf1rxhnyj3td FOREIGN KEY (policy_group_id)
        REFERENCES certpolicygroup (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknfd51x2fwhmabw6x53e84v0hs FOREIGN KEY (domain_id)
        REFERENCES domain (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS certpolicygroupreltn
(
    id SERIAL PRIMARY KEY,
    incoming boolean,
    outgoing boolean,
    "policyUse" integer NOT NULL,
    "certPolicyId" bigint NOT NULL,
    "certPolicyGroupId" bigint NOT NULL,
    CONSTRAINT fkb15blxbknfrqfph9leoxblx0g FOREIGN KEY ("certPolicyGroupId")
        REFERENCES certpolicygroup (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkod74ecikb8oaeibc89qb48lmq FOREIGN KEY ("certPolicyId")
        REFERENCES certpolicy (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS dnsrecord
(
    id SERIAL PRIMARY KEY,
    "createTime" timestamp without time zone,
    data bytea,
    dclass integer,
    name character varying(255),
    ttl bigint,
    type integer
);

CREATE TABLE IF NOT EXISTS setting
(
    id SERIAL PRIMARY KEY,
    "createTime" timestamp without time zone,
    name character varying(255),
    status integer,
    "updateTime" timestamp without time zone,
    value character varying(4096),
    CONSTRAINT ukbk4oycm648x0ox633r4m22b7d UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS trustbundle
(
    id SERIAL PRIMARY KEY,
    "bundleName" character varying(255) NOT NULL,
    "bundleURL" character varying(255) NOT NULL,
    "getCheckSum" character varying(255) NOT NULL,
    "createTime" timestamp without time zone NOT NULL,
    "lastRefreshAttempt" timestamp without time zone,
    "lastRefreshError" integer,
    "lastSuccessfulRefresh" timestamp without time zone,
    "refreshInterval" integer,
    "signingCertificateData" bytea,
    CONSTRAINT uk_7wjl5k4628iitl72bqlq6c2i9 UNIQUE ("bundleName")
);


CREATE TABLE IF NOT EXISTS trustbundleanchor
(
    id SERIAL PRIMARY KEY,
    "anchorData" bytea NOT NULL,
    thumbprint character varying(255) NOT NULL,
    "validEndDate" timestamp without time zone NOT NULL,
    "validStartDate" timestamp without time zone NOT NULL,
    "trustBundleId" bigint NOT NULL,
    CONSTRAINT fki612c6ixdinopnt1j6lg63a01 FOREIGN KEY ("trustBundleId")
        REFERENCES trustbundle (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS trustbundledomainreltn
(
    id SERIAL PRIMARY KEY,
    "forIncoming" boolean,
    "forOutgoing" boolean,
    domain_id bigint NOT NULL,
    trust_bundle_id bigint NOT NULL,
    CONSTRAINT fkh0yi3hkis5guunete0nbxetfn FOREIGN KEY (trust_bundle_id)
        REFERENCES trustbundle (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkw1ab87b1hkv95r9f713iwsoo FOREIGN KEY (domain_id)
        REFERENCES domain (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
