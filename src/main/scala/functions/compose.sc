

def compose[A](optF: Option[A => A], optG: Option[A => A]): Option[A => A] =
  optF.fold(optG)(f => optG.fold(Some(f))(g => Some(f andThen g)))



def compose2[A](optF: Option[A => A], optG: Option[A => A]): Option[A => A] = (optF, optG) match {
  case (Some(f), Some(g)) => Some(f andThen g)
  case (_, None) => optF
  case (None, _) => optG
}


def funA(i: Int) = i + 1
def funB(i: Int) = i * 3


def x = compose2(Some(funA _), Some(funB _))
def x2 = compose2(Some(funA _),None)

def test(f:Option[Int => Int])(i: Int) = f.map(_(i))


test(x)(5)
test(x2)(5)


