CREATE TABLE IF NOT EXISTS domain
(
    id SERIAL PRIMARY KEY,
    createtime timestamp without time zone,
    domainname character varying(255),
    postmasteraddressid bigint,
    status integer,
    updatetime timestamp without time zone,
    CONSTRAINT uk_4qyl85kb0l95n82ouqir44d0x UNIQUE (domainname)
);

CREATE TABLE IF NOT EXISTS address
(
    id SERIAL PRIMARY KEY,
    createtime timestamp without time zone,
    displayname character varying(100),
    emailaddress character varying(400),
    endpoint character varying(255),
    status integer,
    type character varying(64) ,
    updatetime timestamp without time zone,
    domainid bigint NOT NULL,
    CONSTRAINT fk2euw2caoptxa2x1unxx28myos FOREIGN KEY (domainid)
        REFERENCES domain (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS anchor
(
    id SERIAL PRIMARY KEY,
    certificateid bigint NOT NULL,
    createtime timestamp without time zone,
    certificatedata bytea,
    forincoming boolean,
    foroutgoing boolean,
    owner character varying(255),
    status integer,
    thumbprint character varying(255),
    validenddate timestamp without time zone,
    validstartdate timestamp without time zone
);

CREATE TABLE IF NOT EXISTS certificate
(
    id SERIAL PRIMARY KEY,
    createtime timestamp without time zone,
    certificatedata bytea,
    owner character varying(255),
    privatekey boolean,
    status integer,
    thumbprint character varying(255),
    validenddate timestamp without time zone,
    validstartdate timestamp without time zone
);

CREATE TABLE IF NOT EXISTS certpolicy
(
    id SERIAL PRIMARY KEY,
    createtime timestamp without time zone NOT NULL,
    lexicon integer NOT NULL,
    data bytea NOT NULL,
    policyname character varying(255),
    CONSTRAINT uk_gxnmqpsot5r835vgl888kgul8 UNIQUE (policyname)
);

CREATE TABLE IF NOT EXISTS certpolicygroup
(
    id SERIAL PRIMARY KEY,
    createtime timestamp without time zone NOT NULL,
    policygroupname character varying(255),
    CONSTRAINT uk_c749eoa4ewcyqou5270tj7r04 UNIQUE (policygroupname)
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
    policyuse integer NOT NULL,
    certpolicyid bigint NOT NULL,
    certpolicygroupid bigint NOT NULL,
    CONSTRAINT fkb15blxbknfrqfph9leoxblx0g FOREIGN KEY (certpolicygroupid)
        REFERENCES certpolicygroup (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkod74ecikb8oaeibc89qb48lmq FOREIGN KEY (certpolicyid)
        REFERENCES certpolicy (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS dnsrecord
(
    id SERIAL PRIMARY KEY,
    createtime timestamp without time zone,
    data bytea,
    dclass integer,
    name character varying(255),
    ttl bigint,
    type integer
);

CREATE TABLE IF NOT EXISTS setting
(
    id SERIAL PRIMARY KEY,
    createtime timestamp without time zone,
    name character varying(255),
    status integer,
    updatetime timestamp without time zone,
    value character varying(4096),
    CONSTRAINT ukbk4oycm648x0ox633r4m22b7d UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS trustbundle
(
    id SERIAL PRIMARY KEY,
    bundlename character varying(255) NOT NULL,
    bundleurl character varying(255) NOT NULL,
    getchecksum character varying(255) NOT NULL,
    createtime timestamp without time zone NOT NULL,
    lastrefreshattempt timestamp without time zone,
    lastrefresherror integer,
    lastsuccessfulrefresh timestamp without time zone,
    refreshinterval integer,
    signingcertificatedata bytea,
    CONSTRAINT uk_7wjl5k4628iitl72bqlq6c2i9 UNIQUE (bundlename)
);


CREATE TABLE IF NOT EXISTS trustbundleanchor
(
    id SERIAL PRIMARY KEY,
    anchordata bytea NOT NULL,
    thumbprint character varying(255) NOT NULL,
    validenddate timestamp without time zone NOT NULL,
    validstartdate timestamp without time zone NOT NULL,
    trustbundleid bigint NOT NULL,
    CONSTRAINT fki612c6ixdinopnt1j6lg63a01 FOREIGN KEY (trustbundleid)
        REFERENCES trustbundle (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS trustbundledomainreltn
(
    id SERIAL PRIMARY KEY,
    forincoming boolean,
    foroutgoing boolean,
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