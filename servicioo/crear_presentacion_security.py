from zipfile import ZipFile, ZIP_DEFLATED
from xml.sax.saxutils import escape
from pathlib import Path

OUT = Path(__file__).with_name('Presentacion_Spring_Security_NETO.pptx')
W, H = 12192000, 6858000
NAVY, BLUE, ORANGE, PALE, WHITE, INK, MUTED = '123B78', '1C5BA8', 'F36B21', 'EEF4FB', 'FFFFFF', '17243A', '64748B'

slides = [
('Seguridad en el sistema de ventas NETO','Implementación de Spring Security en una aplicación React + Spring Boot  ·  Ingeniería de Software',[
('OBJETIVO','Proteger el acceso, las operaciones y los datos del sistema de ventas.'),('STACK','React + Vite  ·  Spring Boot  ·  Spring Security  ·  MySQL'),('EXPOSITOR','Nombre: ____________________     Grupo: ______')]),
('¿Por qué agregar seguridad?','Antes: el cliente podía ocultar pantallas, pero eso por sí solo no protege la API.',[
('PROBLEMA','Una petición HTTP puede enviarse sin usar la interfaz. La autorización visual no basta.'),('SOLUCIÓN','El servidor autentica al usuario y decide si su rol puede ejecutar cada operación.'),('RESULTADO','Las operaciones protegidas responden 401 si falta sesión y 403 si faltan permisos.')]),
('Arquitectura de seguridad','La autorización ocurre en el backend; React solo presenta el estado de sesión.',[
('1 · REACT / VITE','Login, cookie de sesión y token CSRF. Vite reenvía /api a localhost:8080.'),('2 · SPRING SECURITY','SecurityFilterChain valida sesión, CSRF y permisos antes de entrar al controlador.'),('3 · SERVICIOS DE IDENTIDAD','UserDetailsService consulta al usuario y BCrypt verifica la contraseña.'),('4 · MYSQL','UsuarioSistema almacena nombre, hash, rol y estado activo.')]),
('Piezas implementadas en el backend','Responsabilidades separadas siguiendo la estructura por capas del proyecto.',[
('config/SecurityConfig.java','Reglas HTTP, sesión, CSRF, BCrypt y respuestas JSON para 401/403.'),('config/UsuarioDetailsService.java','Busca al usuario activo y convierte su rol en una autoridad Spring.'),('controller/AuthController.java','Endpoints de CSRF, login, sesión actual y cierre de sesión.'),('model + repository + DTO','UsuarioSistema, consulta de usuarios y contratos de entrada/respuesta.')]),
('Usuarios y contraseñas','La contraseña en texto claro no se guarda en la base de datos.',[
('ALMACENAMIENTO','PasswordEncoder usa BCrypt; el usuario mantiene passwordHash, rol y activo.'),('ARRANQUE LOCAL','UsuariosIniciales crea cajero, lider y encargado si no existen.'),('CONFIGURACIÓN','Las claves se pueden definir por NETO_CAJERO_PASSWORD, NETO_LIDER_PASSWORD y NETO_ENCARGADO_PASSWORD.'),('IMPORTANTE','Son cuentas de demostración. Configurar claves propias y desactivar demo-users en un entorno real.')]),
('Roles y permisos','El rol se valida en el backend, aunque alguien intente llamar directamente a la API.',[
('CAJERO','Consultar ventas, productos y sucursales; registrar ventas.'),('LÍDER','Permisos de cajero y actualización de ventas.'),('ENCARGADO','Permisos de líder; borrar ventas, administrar catálogo/sucursales y consultar auditoría.'),('AUDITORÍA','El actor se obtiene de la sesión autenticada; no se confía en un nombre enviado por el navegador.')]),
('Flujo de inicio de sesión','AuthController autentica credenciales y guarda el contexto de seguridad en la sesión HTTP.',[
('1','React solicita GET /api/auth/csrf y obtiene un token.'),('2','React envía usuario, contraseña y token a POST /api/auth/login.'),('3','AuthenticationManager consulta UserDetailsService; BCrypt compara el hash.'),('4','El backend guarda la autenticación en la sesión y responde el perfil y rol.'),('5','GET /api/auth/me restaura la sesión; POST /api/auth/logout la invalida.')]),
('Sesión y protección CSRF','El navegador conserva la sesión; el token CSRF acompaña las operaciones que modifican datos.',[
('COOKIE DE SESIÓN','HttpOnly y SameSite=Lax en la configuración actual.'),('CSRF','CookieCsrfTokenRepository; React envía el token en el encabezado que solicita Spring.'),('VITE','Proxy /api al backend para mantener llamadas del navegador en el mismo origen durante desarrollo.'),('CIERRE','Al salir, se invalida la sesión del servidor y el cliente elimina el estado local.')]),
('Qué cambió en la interfaz','El frontend ya no decide quién es el usuario enviando datos falsificables.',[
('Login.jsx','Envía credenciales a Spring y presenta los errores recibidos.'),('App.jsx','Consulta /me al cargar, conserva el perfil y cierra sesión mediante el backend.'),('sesionHeaders.js','Incluye X-XSRF-TOKEN y ya no envía cabeceras de actor simuladas.'),('CONTROL DE VISTA','La interfaz puede ocultar opciones por rol; la protección real sigue siendo la regla backend.')]),
('Endpoints principales','Las reglas generales viven en SecurityConfig; las restricciones sensibles también usan @PreAuthorize.',[
('PÚBLICOS','GET /api/auth/csrf  ·  POST /api/auth/login'),('SESIÓN','GET /api/auth/me  ·  POST /api/auth/logout'),('VENTAS','GET/POST: roles autenticados  ·  PUT: líder/encargado  ·  DELETE: encargado'),('ADMINISTRACIÓN','Productos y sucursales: lectura autenticada; cambios restringidos al encargado.'),('AUDITORÍA','GET /api/auditoria: encargado únicamente.')]),
('Verificación realizada','La compilación comprueba que las piezas de seguridad están integradas en el código.',[
('BACKEND','Compilación Maven: correcta.'),('FRONTEND','Build de Vite: correcto.'),('EJECUCIÓN','No fue posible validar login en vivo: MySQL en localhost:3306 rechazó la conexión.'),('PARA DEMOSTRAR','Iniciar MySQL y la BD tienda_neto; después Spring Boot y Vite; probar login, permisos y logout.')]),
('Alcance y siguientes pasos','La implementación actual protege la API con sesión y roles para una demostración funcional.',[
('ANTES DE PRODUCCIÓN','Usar secretos fuera del repositorio, HTTPS, contraseñas robustas y gestión segura de usuarios.'),('ESCALAR A SUCURSALES','Relacionar cada usuario con sucursal/caja y comprobar ese alcance en las consultas de ventas.'),('ENDURECIMIENTO','Agregar límites de intentos, recuperación de cuenta, cambios de contraseña y monitoreo.'),('IDEA CLAVE','Spring Security autentica quién es el usuario y autoriza qué puede hacer.')]),
('Referencias','Documentación oficial consultada',[
('SPRING SECURITY','docs.spring.io/spring-security/reference/getting-spring-security.html'),('PASSWORD STORAGE','docs.spring.io/spring-security/reference/features/authentication/password-storage.html'),('AUTORIZACIÓN','docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html'),('CSRF','docs.spring.io/spring-security/reference/servlet/exploits/csrf.html')]),
]

def xml_text(s): return escape(str(s), {'"':'&quot;'})
def shape_text(idx, name, x,y,cx,cy, text, size=1800, color=INK, bold=False, fill=None, rounded=False):
    geom = 'roundRect' if rounded else 'rect'
    fill_xml = f'<a:solidFill><a:srgbClr val="{fill}"/></a:solidFill>' if fill else '<a:noFill/>'
    lines = str(text).split('\n')
    paras = ''.join(f'<a:p><a:pPr/><a:r><a:rPr lang="es-MX" sz="{size}" b="{1 if bold else 0}"><a:solidFill><a:srgbClr val="{color}"/></a:solidFill><a:latin typeface="Aptos"/></a:rPr><a:t>{xml_text(line)}</a:t></a:r><a:endParaRPr lang="es-MX" sz="{size}"/></a:p>' for line in lines)
    return f'<p:sp><p:nvSpPr><p:cNvPr id="{idx}" name="{xml_text(name)}"/><p:cNvSpPr/><p:nvPr/></p:nvSpPr><p:spPr><a:xfrm><a:off x="{x}" y="{y}"/><a:ext cx="{cx}" cy="{cy}"/></a:xfrm><a:prstGeom prst="{geom}"><a:avLst/></a:prstGeom>{fill_xml}<a:ln><a:noFill/></a:ln></p:spPr><p:txBody><a:bodyPr wrap="square" lIns="150000" rIns="120000" tIns="80000" bIns="50000" anchor="ctr"/><a:lstStyle/>{paras}</p:txBody></p:sp>'

def make_slide(title, subtitle, cards, n):
    arr=[]; i=2
    # background, top brand ribbon, heading
    arr.append(shape_text(i,'Background',0,0,W,H,'',fill=PALE)); i+=1
    arr.append(shape_text(i,'Brand',0,0,W,330000,'',fill=NAVY)); i+=1
    arr.append(shape_text(i,'Brand label',380000,30000,3000000,260000,'NETO  |  SISTEMA DE VENTAS',1100,WHITE,True)); i+=1
    arr.append(shape_text(i,'Title',440000,590000,11300000,700000,title,3000,NAVY,True)); i+=1
    if subtitle:
        arr.append(shape_text(i,'Subtitle',460000,1290000,11200000,400000,subtitle,1500,MUTED)); i+=1
    count=len(cards); top=1900000 if subtitle else 1650000; gap=170000; avail=H-top-650000
    card_h=min(1120000, (avail-(count-1)*gap)//count)
    for j,(head,body) in enumerate(cards):
        y=top+j*(card_h+gap)
        arr.append(shape_text(i,'Card '+str(j),480000,y,11100000,card_h,'',fill=WHITE,rounded=True)); i+=1
        arr.append(shape_text(i,'Card heading '+str(j),650000,y+50000,10600000,260000,head,1450,ORANGE,True)); i+=1
        arr.append(shape_text(i,'Card body '+str(j),650000,y+330000,10600000,card_h-370000,body,1550,INK)); i+=1
    arr.append(shape_text(i,'Footer',450000,H-420000,10000000,250000,'Sistema de ventas NETO  ·  Spring Security',950,MUTED)); i+=1
    arr.append(shape_text(i,'Page',11000000,H-420000,500000,250000,str(n).zfill(2),950,BLUE,True));
    tree='<p:nvGrpSpPr><p:cNvPr id="1" name=""/><p:cNvGrpSpPr/><p:nvPr/></p:nvGrpSpPr><p:grpSpPr><a:xfrm><a:off x="0" y="0"/><a:ext cx="0" cy="0"/><a:chOff x="0" y="0"/><a:chExt cx="0" cy="0"/></a:xfrm></p:grpSpPr>'
    return f'<?xml version="1.0" encoding="UTF-8" standalone="yes"?><p:sld xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main"><p:cSld><p:spTree>{tree}{"".join(arr)}</p:spTree></p:cSld><p:clrMapOvr><a:masterClrMapping/></p:clrMapOvr></p:sld>'

def rels(items): return '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'+''.join(f'<Relationship Id="rId{i+1}" Type="{t}" Target="{target}"/>' for i,(t,target) in enumerate(items))+'</Relationships>'

with ZipFile(OUT,'w',ZIP_DEFLATED) as z:
    z.writestr('[Content_Types].xml','''<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types"><Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/><Default Extension="xml" ContentType="application/xml"/><Override PartName="/ppt/presentation.xml" ContentType="application/vnd.openxmlformats-officedocument.presentationml.presentation.main+xml"/><Override PartName="/ppt/slideMasters/slideMaster1.xml" ContentType="application/vnd.openxmlformats-officedocument.presentationml.slideMaster+xml"/><Override PartName="/ppt/slideLayouts/slideLayout1.xml" ContentType="application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml"/><Override PartName="/ppt/theme/theme1.xml" ContentType="application/vnd.openxmlformats-officedocument.theme+xml"/>'''+''.join(f'<Override PartName="/ppt/slides/slide{i}.xml" ContentType="application/vnd.openxmlformats-officedocument.presentationml.slide+xml"/>' for i in range(1,len(slides)+1))+'''</Types>''')
    z.writestr('_rels/.rels',rels([('http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument','ppt/presentation.xml')]))
    sids=''.join(f'<p:sldId id="{255+i}" r:id="rId{i+1}"/>' for i in range(len(slides)))
    z.writestr('ppt/presentation.xml',f'''<?xml version="1.0" encoding="UTF-8" standalone="yes"?><p:presentation xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main"><p:sldMasterIdLst><p:sldMasterId id="2147483648" r:id="rId{len(slides)+1}"/></p:sldMasterIdLst><p:sldIdLst>{sids}</p:sldIdLst><p:sldSz cx="{W}" cy="{H}" type="wide"/><p:notesSz cx="6858000" cy="9144000"/></p:presentation>''')
    links=[('http://schemas.openxmlformats.org/officeDocument/2006/relationships/slide',f'slides/slide{i}.xml') for i in range(1,len(slides)+1)]+[('http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideMaster','slideMasters/slideMaster1.xml')]
    z.writestr('ppt/_rels/presentation.xml.rels',rels(links))
    z.writestr('ppt/slideMasters/slideMaster1.xml','''<?xml version="1.0" encoding="UTF-8" standalone="yes"?><p:sldMaster xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main"><p:cSld><p:spTree><p:nvGrpSpPr><p:cNvPr id="1" name=""/><p:cNvGrpSpPr/><p:nvPr/></p:nvGrpSpPr><p:grpSpPr><a:xfrm><a:off x="0" y="0"/><a:ext cx="0" cy="0"/><a:chOff x="0" y="0"/><a:chExt cx="0" cy="0"/></a:xfrm></p:grpSpPr></p:spTree></p:cSld><p:clrMap accent1="1C5BA8" accent2="F36B21" accent3="123B78" accent4="EEF4FB" accent5="64748B" accent6="17243A" bg1="FFFFFF" bg2="EEF4FB" tx1="17243A" tx2="64748B" hlink="1C5BA8" folHlink="123B78"/><p:sldLayoutIdLst><p:sldLayoutId id="1" r:id="rId1"/></p:sldLayoutIdLst><p:txStyles><p:titleStyle/><p:bodyStyle/><p:otherStyle/></p:txStyles></p:sldMaster>''')
    z.writestr('ppt/slideMasters/_rels/slideMaster1.xml.rels',rels([('http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout','../slideLayouts/slideLayout1.xml'),('http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme','../theme/theme1.xml')]))
    z.writestr('ppt/slideLayouts/slideLayout1.xml','''<?xml version="1.0" encoding="UTF-8" standalone="yes"?><p:sldLayout xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main" type="blank" preserve="1"><p:cSld name="Blank"><p:spTree><p:nvGrpSpPr><p:cNvPr id="1" name=""/><p:cNvGrpSpPr/><p:nvPr/></p:nvGrpSpPr><p:grpSpPr><a:xfrm><a:off x="0" y="0"/><a:ext cx="0" cy="0"/><a:chOff x="0" y="0"/><a:chExt cx="0" cy="0"/></a:xfrm></p:grpSpPr></p:spTree></p:cSld><p:clrMapOvr><a:masterClrMapping/></p:clrMapOvr></p:sldLayout>''')
    z.writestr('ppt/slideLayouts/_rels/slideLayout1.xml.rels',rels([('http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideMaster','../slideMasters/slideMaster1.xml')]))
    z.writestr('ppt/theme/theme1.xml','''<?xml version="1.0" encoding="UTF-8" standalone="yes"?><a:theme xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" name="NETO"><a:themeElements><a:clrScheme name="Neto"><a:dk1><a:srgbClr val="17243A"/></a:dk1><a:lt1><a:srgbClr val="FFFFFF"/></a:lt1><a:dk2><a:srgbClr val="123B78"/></a:dk2><a:lt2><a:srgbClr val="EEF4FB"/></a:lt2><a:accent1><a:srgbClr val="1C5BA8"/></a:accent1><a:accent2><a:srgbClr val="F36B21"/></a:accent2><a:accent3><a:srgbClr val="64748B"/></a:accent3><a:accent4><a:srgbClr val="DCE8F6"/></a:accent4><a:accent5><a:srgbClr val="F9A36E"/></a:accent5><a:accent6><a:srgbClr val="A9BED9"/></a:accent6><a:hlink><a:srgbClr val="1C5BA8"/></a:hlink><a:folHlink><a:srgbClr val="123B78"/></a:folHlink></a:clrScheme><a:fontScheme name="Aptos"><a:majorFont><a:latin typeface="Aptos Display"/></a:majorFont><a:minorFont><a:latin typeface="Aptos"/></a:minorFont></a:fontScheme><a:fmtScheme name="Neto"><a:fillStyleLst/><a:lnStyleLst/><a:effectStyleLst/><a:bgFillStyleLst/></a:fmtScheme></a:themeElements></a:theme>''')
    for i,(title,subtitle,cards) in enumerate(slides,1):
        z.writestr(f'ppt/slides/slide{i}.xml',make_slide(title,subtitle,cards,i))
        z.writestr(f'ppt/slides/_rels/slide{i}.xml.rels',rels([('http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout','../slideLayouts/slideLayout1.xml')]))
print(OUT)
